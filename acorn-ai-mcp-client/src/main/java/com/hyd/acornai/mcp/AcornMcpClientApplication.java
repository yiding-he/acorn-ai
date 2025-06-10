package com.hyd.acornai.mcp;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@SpringBootApplication
public class AcornMcpClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(AcornMcpClientApplication.class, args);
  }

  @Bean
  public ChatClient chatClient(ChatClient.Builder builder, List<McpSyncClient> mcpSyncClients) {
    return builder
      .defaultSystem("""
        你是一位知识库管理者，通过 Acorn-AI MCP Server 来管理知识库内容。具体规则如下：
        1. 分析用户是在进行提问还是在提供信息。如果不能明确，请向用户提出明确要求，且不操作知识库。
        2. 当明确用户是在提供信息时，在知识库中搜索相关信息是否已经存在，然后进行如下判断：
          a. 如果用户信息在知识库中已经存在且内容一致，则忽略该用户信息；
          b. 如果用户信息在知识库中已经存在但内容不一致，则替换知识库中的相关内容；
          c. 如果用户信息在知识库中不存在，则将用户信息添加到知识库。
          d. 注意：用户提供的信息可能同时包含上面三种情况，Acorn-AI MCP Server 接口可以一次性进行添加和替换操作。
        3. 当明确用户是在提问时，在知识库中搜索相关信息是否已经存在，然后仅根据搜索结果进行回答，不要回答与搜索结果无关的内容。""")
      .defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients))
      .defaultAdvisors(MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().build()).build())
      .build();
  }

  @Controller
  public static class MvcController {

    @Autowired
    private ChatClient chatClient;

    @RequestMapping(value = "/chat", produces = "text/plain")
    @ResponseBody
    public String chat(@RequestParam("msg") String msg) {
      return chatClient.prompt(msg).call().content();
    }
  }
}
