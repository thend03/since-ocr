package com.since.ocr.service;

import java.util.List;

/**
 * ocr service
 *
 * @author since
 * @date 2023-04-11 18:43
 **/
public interface OcrService {

    /**
     * @return ocr type
     */
    String getOcrType();

    /**
     * 获取ocr的结果
     *
     * @param imageBase64 base64
     * @return ocr result
     */
    List<String> getOcrResult(String imageBase64);

    /**
     * http接口返回值处理
     *
     * @param message message
     * @return ocr list
     */
    List<String> parseOcrResult(String message);


}