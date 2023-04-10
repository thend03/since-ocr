package com.since.ocr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.since.ocr.config.GoogleTranslateConfig;
import com.since.ocr.config.PaddleOcrConfig;
import com.since.ocr.config.YoudaoTranslateConfig;
import com.since.ocr.model.GoogleSentences;
import com.since.ocr.model.GoogleTranslateResult;
import com.since.ocr.model.TranslateResult;
import com.since.ocr.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * ocr controller
 *
 * @author since
 * @date 2023-04-09 14:13
 */
@Slf4j
@Controller
public class OcrController {

    /**
     * 上传地址
     */
    @Value("${file.upload.path}")
    private String filePath;
    @Autowired
    private PaddleOcrConfig paddleOcrConfig;
    @Autowired
    private YoudaoTranslateConfig youdaoTranslateConfig;
    @Autowired
    private GoogleTranslateConfig googleTranslateConfig;

    @RequestMapping("/pic/upload")
    public String upload(@RequestParam("file") MultipartFile file, Model model) {
        log.info("pic upload and parse, file name: {}", file.getOriginalFilename());
        String base64String = "";
        try {
            // 写入文件
//            file.transferTo(new File(path + File.separator + filename));
            base64String = getBase64String(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] base64Array = new String[]{base64String};
        Map<String, Object> params = new HashMap<>(2);
        params.put("images", base64Array);
        String ocrResult = getOcrResult(params);
        List<String> msgList = parseOcrResult(ocrResult);
        List<TranslateResult> translate = translate(msgList);
        // 将src路径发送至html页面
        model.addAttribute("translateList", translate);
        return "index";
    }

    private String getBase64String(MultipartFile multiPartFile) throws IOException {
        String baseStr;
        Base64.Encoder encoder = Base64.getEncoder();
        baseStr = new String(encoder.encode(multiPartFile.getBytes()));
        baseStr = baseStr.replaceAll("\r\n", "");
        return baseStr;
    }

    private String getOcrResult(Map<String, Object> params) {
        String url = paddleOcrConfig.getUrl();
        return OkHttpUtil.getInstance().postApplicationJson(url, null, params);
    }

    private static List<String> parseOcrResult(String message) {
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

    private List<TranslateResult> translate(List<String> msgList) {
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

    private String parseTranslateResult(String message) {
        log.info("parseTranslateResult, message: {}", message);
        if (StringUtils.isBlank(message)) {
            return "翻译失败，返回结果为空";
        }
        GoogleTranslateResult googleTranslateResult = JSON.parseObject(message, GoogleTranslateResult.class);
        List<GoogleSentences> sentences = googleTranslateResult.getSentences();
        if (CollectionUtils.isEmpty(sentences)) {
            return "翻译失败,sentences为空";
        }
        List<String> tmp = new ArrayList<>(sentences.size());
        for (GoogleSentences googleSentences : sentences) {
            String trans = googleSentences.getTrans();
            tmp.add(trans);
        }
        return StringUtils.join(tmp, ",");
    }


}
