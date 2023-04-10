package com.since.ocr.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

/**
 * @author since
 * @date 2023-04-09 14:29
 */
@Controller
public class IndexController {

    /**
     * 上传地址
     */
    @Value("${file.upload.path}")
    private String filePath;

    // 跳转上传页面
    @RequestMapping("test")
    public String test() {
        return "index";
    }

    @RequestMapping("/google")
    public String google() {
        return "google";
    }
}
