package com.hyd.acornai.kb;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalFileAcordKnowledgeBaseTest {

  @Test
  void createLocalFileAcordKnowledgeBase() throws Exception {
    var apiKey = System.getenv().get("ALIYUN_LLM_API_KEY");
    assertTrue(StringUtils.hasText(apiKey));

    OpenAiApi api = OpenAiApi.builder()
      .baseUrl("https://dashscope.aliyuncs.com/compatible-mode")
      .apiKey(apiKey)
      .build();

    OpenAiEmbeddingOptions embeddingOptions = OpenAiEmbeddingOptions.builder().model("text-embedding-v3").build();
    EmbeddingModel embeddingModel = new OpenAiEmbeddingModel(api, MetadataMode.EMBED, embeddingOptions);

    LocalFileAcordKnowledgeBase kb = new LocalFileAcordKnowledgeBase(
      SimpleVectorStore.builder(embeddingModel).build(),
      Paths.get("data/vector-store.data")
    );

    if (kb.isEmpty()) {
      Function<String, String> readFile = fileName -> {
        try {
          return Files.readString(Path.of("src/test/resources/articles/" + fileName));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      };

      kb.add(List.of(
        new Article(UUID.randomUUID().toString(), readFile.apply("1.txt")),
        new Article(UUID.randomUUID().toString(), readFile.apply("2.txt"))
      ));
    }

    Consumer<String> search = keyword -> {
      var articles = kb.findSimilar(keyword, 1);
      for (Article article : articles) {
        System.out.println("[" + article.getId() + "]");
        System.out.println(article.getContent());
      }
    };

    search.accept("贸易赤字");
    search.accept("犯罪嫌疑人");
  }
}
