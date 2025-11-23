package com.library.controller;

import com.library.common.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class HealthController {
    
    @GetMapping("/api/health")
    public Result<String> health() {
        return Result.success("OK");
    }
    
    @GetMapping("/")
    public Result<Map<String, String>> root() {
        Map<String, String> info = new HashMap<>();
        info.put("message", "图书管理系统API");
        info.put("version", "1.0.0");
        info.put("health", "/api/health");
        info.put("docs", "请查看 API_DOCUMENTATION.md");
        return Result.success(info);
    }
}
