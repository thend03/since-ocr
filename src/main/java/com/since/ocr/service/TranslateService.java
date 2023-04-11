package com.since.ocr.service;

import com.since.ocr.model.TranslateResult;

import java.util.List;

/**
 * translate service
 *
 * @author since
 * @date 2023-04-11 18:58
 **/
public interface TranslateService {
    /**
     * @return translate engine
     */
    String getTranslateType();

    /**
     * @param msgList msg list
     * @return translate result
     */
    List<TranslateResult> translate(List<String> msgList);

    /**
     * @param message message
     * @return final translate message
     */
    String parseTranslateResult(String message);


}