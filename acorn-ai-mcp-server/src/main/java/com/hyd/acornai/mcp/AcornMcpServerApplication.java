package com.hyd.acornai.mcp;

import com.hyd.acornai.mcp.services.TableOfContentService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AcornMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcornMcpServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider tableOfContentTools(TableOfContentService service) {
        return MethodToolCallbackProvider.builder().toolObjects(service).build();
    }
}
