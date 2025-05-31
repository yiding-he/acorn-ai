package com.hyd.acornai.mcp.services;

import com.hyd.acornai.kb.Article;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TableOfContentService {

    @Tool(description = "列出所有的文章，返回每篇文章的ID和标题")
    public List<Article> listArticles() {
        return List.of(
                new Article(UUID.randomUUID().toString(), "与科技相关的内容"),
                new Article(UUID.randomUUID().toString(), "与金融相关的内容"),
                new Article(UUID.randomUUID().toString(), "与体育相关的内容")
        );
    }
}
