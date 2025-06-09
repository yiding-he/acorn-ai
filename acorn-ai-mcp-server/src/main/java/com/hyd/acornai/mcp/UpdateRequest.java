package com.hyd.acornai.mcp;

import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;
import java.util.Map;

@Data
public class UpdateRequest {

  @ToolParam(description = "需要向知识库中添加的文本，每条文本会作为单独的文章或记录存放到向量数据库")
  private List<String> newArticles;

  @ToolParam(description = "需要从知识库中删除的文章ID列表")
  private List<String> deletedArticleIds;

  @ToolParam(description = "需要替换的文章ID及对应的内容。知识库将会删除该ID的文章，并将内容保存为新的文章")
  private Map<String, String> replacedArticles;
}
