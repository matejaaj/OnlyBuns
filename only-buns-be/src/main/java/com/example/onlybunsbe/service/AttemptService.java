package com.example.onlybunsbe.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long TIME_WINDOW = 60 * 1000L; // 1 minute in milliseconds

    // Store login attempts with IP address as the key
    private final Map<String, List<Long>> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String ipAddress) {
        List<Long> timestamps = attempts.get(ipAddress);
        if (timestamps == null) {
            return false;
        }

        // Filter out attempts older than 1 minute
        long currentTime = Instant.now().toEpochMilli();
        List<Long> recentAttempts = timestamps.stream()
                .filter(timestamp -> currentTime - timestamp < TIME_WINDOW)
                .collect(Collectors.toList());

        // Update the attempt list to remove outdated entries
        attempts.put(ipAddress, recentAttempts);

        // Block if attempts exceed the maximum allowed within the time window
        return recentAttempts.size() >= MAX_ATTEMPTS;
    }

    public void recordAttempt(String ipAddress) {
        long currentTime = Instant.now().toEpochMilli();
        attempts.computeIfAbsent(ipAddress, k -> new java.util.ArrayList<>()).add(currentTime);
    }

    public void clearAttempts(String ipAddress) {
        attempts.remove(ipAddress);
    }
}
