package com.since.ocr.enums;

/**
 * translate type enum
 *
 * @author since
 * @date 2023-04-11 20:04
 **/
public enum TranslateTypeEnum {
    /**
     * google翻译
     */
    GOOGLE("google", "谷歌翻译"),
    /**
     * 有道翻译
     */
    YOUDAO("youdao", "有道翻译"),
    /**
     * 百度翻译
     */
    BAIDU("baidu", "百度翻译");
    private String type;
    private String desc;

    TranslateTypeEnum(String type, String desc) {
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