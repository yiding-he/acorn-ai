package com.hyd.acornai.mcp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("acorn-ai")
@Data
public class AcornConfig {

  private String vectorStoreLocalPath;
}
