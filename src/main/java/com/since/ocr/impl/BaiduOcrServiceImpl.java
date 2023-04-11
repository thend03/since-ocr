package com.since.ocr.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.since.ocr.config.BaiduOcrConfig;
import com.since.ocr.enums.OcrTypeEnum;
import com.since.ocr.service.OcrService;
import com.since.ocr.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * baidu ocr service impl
 *
 * @author since
 * @date 2023-04-11 18:45
 */
@Slf4j
@Service
public class BaiduOcrServiceImpl implements OcrService {
    @Autowired
    private BaiduOcrConfig baiduOcrConfig;

    @Override
    public String getOcrType() {
        return OcrTypeEnum.BAIDU_OCR.getType();
    }

    @Override
    public List<String> getOcrResult(String imageBase64) {
        List<String> result = new ArrayList<>();
        try {
            String ocrUrl = baiduOcrConfig.getOcrUrl();
            String baiduOcrAccessToken = getBaiduOcrAccessToken();
            String url = String.format(ocrUrl, baiduOcrAccessToken);
            Map<String, String> params = new HashMap<>(2);
            params.put("image", imageBase64);
            String s = OkHttpUtil.getInstance().postApplicationXWwwFormUrlencoded(url, null, params);
            result = parseOcrResult(s);
        } catch (Exception e) {
            log.error("baidu get ocr result error", e);
            result.add("ocr识别失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<String> parseOcrResult(String message) {
        JSONObject jsonObject = JSON.parseObject(message);
        JSONArray wordsResult = jsonObject.getJSONArray("words_result");
        List<String> strings = wordsResult.toJavaList(String.class);
        StringBuilder sb = new StringBuilder();
        strings.forEach(s -> {
            JSONObject words = JSON.parseObject(s);
            sb.append(words.getString("words")).append(" ");
        });
        List<String> result = new ArrayList<>();
        result.add(sb.toString());
        return result;
    }

    private String getBaiduOcrAccessToken() {
        String accessTokenUrl = baiduOcrConfig.getAccessTokenUrl();
        String apiKey = baiduOcrConfig.getApiKey();
        String secretKey = baiduOcrConfig.getSecretKey();
        String url = String.format(accessTokenUrl, apiKey, secretKey);
        String s = OkHttpUtil.getInstance().postApplicationJson(url, null, null);
        String result = "";
        try {
            JSONObject jsonObject = JSON.parseObject(s);
            result = jsonObject.getString("access_token");
        } catch (Exception e) {
            log.error("get access token error", e);
            result = e.getMessage();
        }
        return result;
    }
}
