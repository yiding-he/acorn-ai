package com.hyd.acornai.kb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.tool.annotation.ToolParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

  @ToolParam(description = "文章ID")
  private String id;

  @ToolParam(description = "文章内容")
  private String content;
}
