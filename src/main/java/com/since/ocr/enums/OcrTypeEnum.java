package com.since.ocr.enums;

/**
 * ocr type enum
 *
 * @author since
 * @date 2023-04-11 18:53
 **/
public enum OcrTypeEnum {
    /**
     * 百度智能云ocr api
     */
    BAIDU_OCR("baidu_ocr", "百度ocr"),

    /**
     * 本地基于paddle起的服务
     */
    PADDLE_OCR("paddle_ocr", "paddle ocr");


    private String type;
    private String desc;

    OcrTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}