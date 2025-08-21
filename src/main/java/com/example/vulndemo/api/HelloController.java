package com.example.vulndemo.api;

import com.example.vulndemo.lib.LegacyLibUsage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final LegacyLibUsage legacy = new LegacyLibUsage();

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        // Call methods that use the outdated libs to ensure they're on the classpath
        int size = legacy.sampleGuavaListSize();
        String clientInfo = legacy.buildHttpClientInfo();
        String mapType = legacy.sampleCommonsCollectionsMapType();

        return Map.of(
                "message", "ok",
                "guavaListSize", size,
                "httpClient", clientInfo,
                "commonsCollectionsMap", mapType
        );
    }
}
