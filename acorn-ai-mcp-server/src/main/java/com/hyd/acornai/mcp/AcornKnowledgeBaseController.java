package com.hyd.acornai.mcp;

import com.hyd.acornai.kb.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge-base")
public class AcornKnowledgeBaseController {

  @Autowired
  private AcornKnowledgeBaseService knowledgeBaseService;

  /**
   * 更新知识库内容（新增、删除、替换）
   */
  @PostMapping("/update")
  public String updateKnowledgeBase(@RequestBody UpdateRequest request) {
    return knowledgeBaseService.update(request);
  }

  /**
   * 根据关键词搜索知识库内容
   */
  @GetMapping("/search")
  public List<Article> searchKnowledgeBase(
    @RequestParam("keyword") String keyword,
    @RequestParam(value = "topK", required = false, defaultValue = "5") int topK,
    @RequestParam(value = "threshold", required = false, defaultValue = "0.7") double threshold
  ) {
    return knowledgeBaseService.search(keyword, topK, threshold);
  }
}
