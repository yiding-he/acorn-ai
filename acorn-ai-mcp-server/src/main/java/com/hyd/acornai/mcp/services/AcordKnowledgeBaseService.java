package com.hyd.acornai.mcp.services;

import com.hyd.acornai.kb.AcordKnowledgeBase;
import com.hyd.acornai.kb.Article;
import com.hyd.acornai.mcp.UpdateRequest;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;
import java.util.UUID;

public class AcordKnowledgeBaseService {

  private final AcordKnowledgeBase knowledgeBase;

  public AcordKnowledgeBaseService(AcordKnowledgeBase knowledgeBase) {
    this.knowledgeBase = knowledgeBase;
  }

  @Tool(description = "修改知识库的内容")
  public String update(@ToolParam(description = "需要进行修改的操作，包含增加、删除和替换") UpdateRequest request) {
    this.knowledgeBase.add(request.getNewArticles().stream().map(text -> new Article(UUID.randomUUID().toString(), text)).toList());
    this.knowledgeBase.delete(request.getDeletedArticleIds());
    request.getReplacedArticles().forEach((id, text) -> this.knowledgeBase.update(new Article(id, text)));
    return "Finished successfully.";
  }

  @Tool(description = "搜索知识库内容")
  public List<Article> search(@ToolParam(description = "搜索关键字") String searchKeyword) {
    return knowledgeBase.findSimilar(searchKeyword, 5);
  }
}
