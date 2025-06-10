package com.hyd.acornai.mcp;

import com.hyd.acornai.kb.AcordKnowledgeBase;
import com.hyd.acornai.kb.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;
import java.util.UUID;

@Slf4j
public class AcornKnowledgeBaseService {

  private final AcordKnowledgeBase knowledgeBase;

  public AcornKnowledgeBaseService(AcordKnowledgeBase knowledgeBase) {
    this.knowledgeBase = knowledgeBase;
  }

  @Tool(description = "修改知识库的内容")
  public String update(@ToolParam(description = "需要进行修改的操作，包含增加、删除和替换") UpdateRequest request) {
    this.knowledgeBase.add(request.getNewArticles().stream().map(text -> new Article(UUID.randomUUID().toString(), text)).toList());
    this.knowledgeBase.delete(request.getDeletedArticleIds());

    var replacedArticles = request.getReplacedArticles();
    if (replacedArticles != null && !replacedArticles.isEmpty()) {
      replacedArticles.forEach((id, text) -> this.knowledgeBase.update(new Article(id, text)));
    }

    return "Finished successfully.";
  }

  @Tool(description = "搜索知识库内容")
  public List<Article> search(
    @ToolParam(description = "搜索关键字") String searchKeyword,
    @ToolParam(description = "搜索结果数量，默认为 5") int topK,
    @ToolParam(description = "搜索结果相似度阈值，默认 0.7") double threshold
  ) {
    var articles = knowledgeBase.findSimilar(searchKeyword, topK, threshold);
    log.info("搜索关键字 keyword={}, topK={}, threshold={}，结果: {}",
      searchKeyword, topK, threshold, articles.stream().map(Article::getContent).toList()
    );
    return articles;
  }
}
