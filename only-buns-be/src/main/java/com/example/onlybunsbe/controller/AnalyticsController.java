package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.service.AnalyticsService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/posts-comments")
    public Map<String, Object> getPostsAndCommentsStats() {
        return analyticsService.getPostsAndCommentsStats();
    }

    @GetMapping("/user-activity")
    public Map<String, Object> getUserActivityStats() {
        return analyticsService.getUserActivityStats();
    }
}