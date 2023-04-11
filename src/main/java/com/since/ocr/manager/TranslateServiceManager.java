package com.since.ocr.manager;

import com.since.ocr.service.TranslateService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * translate service manager
 *
 * @author since
 * @date 2023-04-11 19:58
 */
@Component
public class TranslateServiceManager {
    private final Map<String, TranslateService> translateServiceMap = new HashMap<>(16);
    @Autowired
    private List<TranslateService> translateServiceList;

    @PostConstruct
    public void init() {
        Map<String, TranslateService> collect = translateServiceList.stream().collect(Collectors.toMap(TranslateService::getTranslateType, translateService -> translateService));
        translateServiceMap.putAll(collect);
    }

    public TranslateService getTranslateService(String type) {
        return translateServiceMap.get(type);
    }
}
