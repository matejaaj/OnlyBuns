package com.example.onlybunsbe.util;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class LimiterManager {
    final ConcurrentMap<String, RateLimiter> keyRateLimiters = new ConcurrentHashMap<>();

    final RateLimiterConfig config = RateLimiterConfig.custom()
            .timeoutDuration(Duration.ofMillis(1000))
            .limitRefreshPeriod(Duration.ofHours(1))    // 1h period
            .limitForPeriod(60)                         // 60 komentara po satu
            .build();

    public RateLimiter getLimiter(String apiKey) {
        return keyRateLimiters.compute(apiKey, (key, limiter) -> {
            return (limiter == null) ? RateLimiter.of(apiKey, config) : limiter;
        });
    }
}
