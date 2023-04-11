package com.since.ocr.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.since.ocr.config.PaddleOcrConfig;
import com.since.ocr.enums.OcrTypeEnum;
import com.since.ocr.service.OcrService;
import com.since.ocr.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * paddle ocr service impl
 *
 * @author feng.chuang
 * @date 2023-04-11 19:51
 */
@Slf4j
@Service
public class PaddleOcrServiceImpl implements OcrService {
    @Autowired
    private PaddleOcrConfig paddleOcrConfig;

    @Override
    public String getOcrType() {
        return OcrTypeEnum.PADDLE_OCR.getType();
    }

    @Override
    public List<String> getOcrResult(String imageBase64) {
        String[] base64Array = new String[]{imageBase64};
        Map<String, Object> params = new HashMap<>(2);
        params.put("images", base64Array);
        String url = paddleOcrConfig.getUrl();
        String s = OkHttpUtil.getInstance().postApplicationJson(url, null, params);
        return parseOcrResult(s);
    }

    @Override
    public List<String> parseOcrResult(String message) {
        log.info("parseOcrResult, ocr result: {}", message);
        if (StringUtils.isBlank(message)) {
            return Collections.singletonList("ocr识别失败");
        }
        JSONObject jsonObject = JSON.parseObject(message);
        String status = jsonObject.getString("status");
        String msg = jsonObject.getString("msg");
        if (!"000".equals(status)) {
            return Collections.singletonList("ocr识别失败,错误原因: " + msg);
        }
        JSONArray results = jsonObject.getJSONArray("results");
        if (results == null || results.size() == 0) {
            return Collections.singletonList("ocr识别,返回结果为空");
        }
        List<String> msgList = new ArrayList<>(results.size());
        for (int i = 0; i < results.size(); i++) {
            Object o = results.get(i);
            if (Objects.isNull(o)) {
                continue;
            }
            if (o instanceof JSONArray textArray) {
                if (textArray.size() == 0) {
                    continue;
                }
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < textArray.size(); j++) {
                    Object textObj = textArray.get(j);
                    String s = JSON.toJSONString(textObj);
                    JSONObject text = JSON.parseObject(s);
                    String finMsg = text.getString("text");
                    sb.append(finMsg).append(" ");
                    System.out.println(finMsg);
                }
                msgList.add(sb.toString());
            }
        }

        return msgList;
    }


}
