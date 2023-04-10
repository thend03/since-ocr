package com.since.ocr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * paddle ocr config
 *
 * @author since
 * @date 2023-04-10 16:02
 */
@Data
@Component
@ConfigurationProperties(prefix = "paddle.ocr")
public class PaddleOcrConfig {
    private String url;
}
