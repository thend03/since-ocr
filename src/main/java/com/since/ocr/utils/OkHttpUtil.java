package com.since.ocr.utils;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * okhttp util
 *
 * @author since
 * @date 2023-04-10 15:46
 */
public class OkHttpUtil {
    private final Request.Builder requestBuilder;
    private final OkHttpClient okHttpClient;

    private OkHttpUtil() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        okHttpClient = builder.connectTimeout(5, TimeUnit.SECONDS).build();
        //省的每次都new  request操作,直接builder出来,随后需要什么往里加,build出来即可
        requestBuilder = new Request.Builder();
    }

    private static final class OkHttpUtilHolder {
        static final OkHttpUtil OK_HTTP_UTIL = new OkHttpUtil();
    }

    public static OkHttpUtil getInstance() {
        return OkHttpUtilHolder.OK_HTTP_UTIL;
    }


    public String postApplicationXWwwFormUrlencoded(String url, Headers headers, String data) throws IOException {
        FormBody formBody = new FormBody.Builder().add("data", data).build();
        if (Objects.nonNull(headers)) {
            requestBuilder.headers(headers);
        }
        Request request = requestBuilder.url(url).post(formBody).build();
        Response response = okHttpClient.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    public String postApplicationJson(String url,Headers headers,Map<String,Object> params) {
        if (Objects.nonNull(headers)) {
            requestBuilder.headers(headers);
        }
        Request request = new Request.Builder().url(url).post(RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(params))).addHeader("Content-Type", "application/json").build();
        return execute(request);
    }

    public String doGet(String url, Map<String, String> params, Map<String, String> headerMap) {

        StringBuilder sb = processParam(url, params);

        Request.Builder builder = new Request.Builder();
        if (headerMap != null && headerMap.size() > 0) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.url(sb.toString()).build();
        return execute(request);
    }

    public String doGet(String url) {
        Request request = new Request.Builder().url(url).build();
        return execute(request);
    }

    private String execute(Request request) {
        String result = "";
        try (Response response = okHttpClient.newCall(request).execute()) {
            return toString(response.body());
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }



    public String upload(String fileName, File file, String url) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .addFormDataPart("fileName", fileName)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        assert response.body() != null;
        return response.body().string();
    }

    private StringBuilder processParam(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (firstFlag) {
                    sb.append("?").append(key).append("=").append(params.get(key));
                    firstFlag = false;
                } else {
                    sb.append("&").append(key).append("=").append(params.get(key));
                }
            }
        }
        return sb;
    }

    /**
     * bytes转String
     *
     * @param body body
     * @return body string
     */
    private String toString(ResponseBody body) {
        String result = "";
        if (null != body) {
            try {
                result = new String(body.bytes());
            } catch (IOException e) {
                result = "";
            }
        }
        return result;
    }
}
