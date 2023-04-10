package com.since.ocr.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * google translate result
 *
 * @author since
 * @date 2023-04-10 20:03
 */
@Data
public class GoogleTranslateResult {
    private List<GoogleSentences> sentences;
    private String src;
    private Double confidence;
    private Map<String,Object> spell;
    @JSONField(name = "ld_result")
    private Map<String,Object> ldResult;
}
