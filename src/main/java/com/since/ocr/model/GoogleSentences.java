package com.since.ocr.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * google sentences
 *
 * @author since
 * @date 2023-04-10 20:08
 */

@Data
public class GoogleSentences {
    private String trans;
    private String orig;
    private Integer backend;
    @JSONField(name = "model_specification")
    private List<JSONObject> modelSpecification;
    @JSONField(name = "translation_engine_debug_info")
    private List<JSONObject> translationEngineDebugInfo;

}
