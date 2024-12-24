package com.example.onlybunsbe.infrastructure.monitoring;

import com.example.onlybunsbe.util.TokenUtils;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ActiveUserTracker {

    private final Map<String, Instant> activeUsers = new ConcurrentHashMap<>();
    private final MeterRegistry meterRegistry;
    private final TokenUtils tokenUtils;

    public ActiveUserTracker(MeterRegistry meterRegistry, TokenUtils tokenUtils) {
        this.meterRegistry = meterRegistry;
        this.tokenUtils = tokenUtils;

        // Registrujte metricu za broj aktivnih korisnika
        meterRegistry.gauge("active_users", activeUsers, Map::size);
    }

    public void trackUser(HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);

        if (userId != null) {
            activeUsers.put(userId, Instant.now());
        }
    }

    public void removeInactiveUsers(long inactivityThresholdMillis) {
        Instant now = Instant.now();
        activeUsers.entrySet().removeIf(entry ->
                now.toEpochMilli() - entry.getValue().toEpochMilli() > inactivityThresholdMillis);
    }

    private String getUserIdFromRequest(HttpServletRequest request) {
        Long userId = tokenUtils.getUserIdFromToken(tokenUtils.getToken(request));
        return userId != null ? String.valueOf(userId) : null;
    }
}
