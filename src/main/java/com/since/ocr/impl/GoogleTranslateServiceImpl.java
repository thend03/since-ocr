package com.since.ocr.impl;

import com.alibaba.fastjson.JSON;
import com.since.ocr.config.GoogleTranslateConfig;
import com.since.ocr.enums.TranslateTypeEnum;
import com.since.ocr.model.GoogleSentences;
import com.since.ocr.model.GoogleTranslateResult;
import com.since.ocr.model.TranslateResult;
import com.since.ocr.service.TranslateService;
import com.since.ocr.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * google translate service impl
 *
 * @author since
 * @date 2023-04-11 20:12
 */
@Slf4j
@Service
public class GoogleTranslateServiceImpl implements TranslateService {
    @Autowired
    private GoogleTranslateConfig googleTranslateConfig;
    @Override
    public String getTranslateType() {
        return TranslateTypeEnum.GOOGLE.getType();
    }

    @Override
    public List<TranslateResult> translate(List<String> msgList) {
        if (CollectionUtils.isEmpty(msgList)) {
            return Collections.emptyList();
        }
        List<TranslateResult> result = new ArrayList<>(msgList.size());
        for (String s : msgList) {
            if (StringUtils.isBlank(s)) {
                continue;
            }
            String url = googleTranslateConfig.getUrl() + URLEncoder.encode(s, StandardCharsets.UTF_8);
            String translate = OkHttpUtil.getInstance().doGet(url);
            String s1 = parseTranslateResult(translate);
            TranslateResult translateResult = new TranslateResult();
            translateResult.setSource(s);
            translateResult.setTarget(s1);
            result.add(translateResult);
        }
        return result;
    }

    @Override
    public String parseTranslateResult(String message) {
        log.info("google translate result parse, message: {}", message);
        if (StringUtils.isBlank(message)) {
            return "谷歌翻译调用失败，返回结果为空";
        }
        GoogleTranslateResult googleTranslateResult = JSON.parseObject(message, GoogleTranslateResult.class);
        List<GoogleSentences> sentences = googleTranslateResult.getSentences();
        if (CollectionUtils.isEmpty(sentences)) {
            return "谷歌翻译调用失败,sentences为空";
        }
        List<String> tmp = new ArrayList<>(sentences.size());
        for (GoogleSentences googleSentences : sentences) {
            String trans = googleSentences.getTrans();
            tmp.add(trans);
        }
        return StringUtils.join(tmp, ",");
    }
}
