package com.since.ocr.manager;

import com.since.ocr.service.OcrService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ocr service manager
 *
 * @author since
 * @date 2023-04-11 18:46
 */
@Component
public class OcrServiceManager {
    private final Map<String, OcrService> ocrServiceMap = new HashMap<>(16);
    @Autowired
    private List<OcrService> ocrServices;

    @PostConstruct
    public void init() {
        Map<String, OcrService> collect = ocrServices.stream().collect(Collectors.toMap(OcrService::getOcrType, ocrService -> ocrService));
        ocrServiceMap.putAll(collect);
    }

    public OcrService getOcrService(String type) {
        return ocrServiceMap.get(type);
    }
}
