package com.since.ocr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.since.ocr.config.GoogleTranslateConfig;
import com.since.ocr.enums.OcrTypeEnum;
import com.since.ocr.enums.TranslateTypeEnum;
import com.since.ocr.manager.TranslateServiceManager;
import com.since.ocr.model.GoogleSentences;
import com.since.ocr.model.GoogleTranslateResult;
import com.since.ocr.model.TranslateResult;
import com.since.ocr.manager.OcrServiceManager;
import com.since.ocr.service.OcrService;
import com.since.ocr.service.TranslateService;
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
    private GoogleTranslateConfig googleTranslateConfig;
    @Autowired
    private OcrServiceManager ocrServiceManager;
    @Autowired
    private TranslateServiceManager translateServiceManager;

    @RequestMapping("/pic/upload")
    public String upload(@RequestParam("file") MultipartFile file, Model model) {
        log.info("pic upload and parse, file name: {}", file.getOriginalFilename());
        String base64String = "";
        try {
            base64String = getBase64String(file);
        } catch (IOException e) {
            log.error("transfer image 2 base64 error", e);
            model.addAttribute("ocrError", e.getMessage());
            return "index";
        }
        try {
            OcrService ocrService = ocrServiceManager.getOcrService(OcrTypeEnum.BAIDU_OCR.getType());
            List<String> msgList = ocrService.getOcrResult(base64String);

            TranslateService translateService = translateServiceManager.getTranslateService(TranslateTypeEnum.GOOGLE.getType());
            List<TranslateResult> translate = translateService.translate(msgList);
            // 将src路径发送至html页面
            model.addAttribute("translateList", translate);
            return "index";
        } catch (Exception e) {
            log.error("ocr or translate error", e);
            model.addAttribute("ocrError", e.getMessage());
            return "index";
        }
    }

    private String getBase64String(MultipartFile multiPartFile) throws IOException {
        String baseStr;
        Base64.Encoder encoder = Base64.getEncoder();
        baseStr = new String(encoder.encode(multiPartFile.getBytes()));
        baseStr = baseStr.replaceAll("\r\n", "");
        return baseStr;
    }


}
