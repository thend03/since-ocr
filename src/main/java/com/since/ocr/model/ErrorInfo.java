package com.since.ocr.model;

import lombok.Data;

/**
 * error info
 *
 * @author since
 * @date 2023-04-11 10:33
 */
@Data
public class ErrorInfo {
    /**
     * 发生时间
     */
    private String time;
    /**
     * 访问Url
     */
    private String url;
    /**
     * 错误类型
     */
    private String error;
    /**
     * 错误的堆栈轨迹
     */
    String stackTrace;
    /**
     * //状态码
     */
    private int statusCode;
    /**
     * 错误原因
     */
    private String reasonPhrase;
}
