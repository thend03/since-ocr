package com.since.ocr.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * paddleocrresult
 *
 * @author since
 * @date 2023-04-10 16:12
 */

public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean status = false;

    private String message;

    private T result;

    private String statusCode;


    /**
     * 返回一个错误的结果集
     *
     * @param statusCode 错误编码
     * @param message    错误信息
     * @return
     */
    public static <T> Result<T> error(String statusCode, String message) {
        return new Result<>(message, null, statusCode);
    }

    /***
     * 返回一个业务处理正常结果集
     * @param data   数据参数
     * @param <T>    泛型
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(true, "操作成功", data, "SYS000");
    }

    public static <T> Result<T> success() {
        return new Result<>(true, "操作成功", null, "SYS000");
    }

    public Result() {
        super();
    }

    public Result(String message, T result, String statusCode) {
        this.message = message;
        this.result = result;
        this.statusCode = statusCode;
    }

    public Result(boolean status, String message, T result, String statusCode) {
        this.status = status;
        this.message = message;
        this.result = result;
        this.statusCode = statusCode;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    /**
     * 提取http返回结果result的JSONObject
     *
     * @return
     */
    public JSONObject getResultJSONObject() {
        return (JSONObject) result;
    }

    /**
     * 提取http返回结果result中的JSONArray
     *
     * @return
     */
    public JSONArray getResultJSONArray() {
        return (JSONArray) result;
    }


    /**
     * 直接从http返回的result中提取对象，返回具体实例
     *
     * @param clazz
     * @return
     */
    public Object getResultBean(Class clazz) {
        return JSON.toJavaObject((JSONObject) result, clazz);
    }

    /**
     * 直接从http返回的result中提取对象，返回具体实例
     *
     * @param clazz
     * @return
     */
    public Object getResultBean(String key, Class clazz) {
        JSONObject obj = ((JSONObject) result).getJSONObject(key);
        return JSON.toJavaObject(obj, clazz);
    }

    /**
     * 直接从http返回的result中提取对象，此时result中直接是一个array
     *
     * @param clazz list中的转化类型
     * @return
     */
    public List getResultBeanList(Class clazz) {
        return JSONObject.parseArray(result.toString()).toJavaList(clazz);
    }

    /**
     * 直接从http返回的result中提取list，并转化成需要的类型
     *
     * @param key   对应需要截取result中的array的字段名称
     * @param clazz list中的转化类型
     * @return
     */
    public List getResultBeanList(String key, Class clazz) {
        JSONArray array;
        try {
            array = ((JSONObject) result).getJSONArray(key);
            return array.toJavaList(clazz);
        } catch (NullPointerException e) {
            return new LinkedList();
        }
    }


    public void setResult(T result) {
        this.result = result;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}

