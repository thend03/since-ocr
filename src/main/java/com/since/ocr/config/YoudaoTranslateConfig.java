package com.since.ocr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * youdao translate config
 *
 * @author since
 * @date 2023-04-10 19:53
 */
@Data
@Component
@ConfigurationProperties(prefix = "youdao.translate")
public class YoudaoTranslateConfig {
    private String url;
}
