package com.hyd.acornai.mcp;

import com.hyd.acornai.kb.LocalFileAcordKnowledgeBase;
import com.hyd.acornai.mcp.services.AcordKnowledgeBaseService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;

@SpringBootApplication
@EnableConfigurationProperties(AcornConfig.class)
public class AcornMcpServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(AcornMcpServerApplication.class, args);
  }

  @Bean
  public ToolCallbackProvider tableOfContentTools(AcordKnowledgeBaseService service) {
    return MethodToolCallbackProvider.builder().toolObjects(service).build();
  }

  @Bean
  public AcordKnowledgeBaseService acordKnowledgeBaseService(EmbeddingModel embeddingModel, AcornConfig config) {
    return new AcordKnowledgeBaseService(new LocalFileAcordKnowledgeBase(
      SimpleVectorStore.builder(embeddingModel).build(),
      Path.of(config.getVectorStoreLocalPath())
    ));
  }
}
