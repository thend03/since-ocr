package com.since.ocr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * google translate config
 *
 * @author feng.chuang
 * @date 2023-04-10 19:55
 */
@Data
@Component
@ConfigurationProperties(prefix = "google.translate")
public class GoogleTranslateConfig {
    private String url;
}
