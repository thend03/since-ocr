package com.since.ocr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023-04-11 17:45
 */
@Data
@Component
@ConfigurationProperties(prefix = "baidu.ocr")
public class BaiduOcrConfig {
    private String accessTokenUrl;
    private String appId;
    private String apiKey;
    private String secretKey;
    private String ocrUrl;


}

