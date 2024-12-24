package com.example.onlybunsbe.infrastructure.monitoring;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ActiveUserCleanupTask {

    private final ActiveUserTracker activeUserTracker;

    public ActiveUserCleanupTask(ActiveUserTracker activeUserTracker) {
        this.activeUserTracker = activeUserTracker;
    }


    @Scheduled(fixedRate = 60000) // 60 sekundi
    public void cleanupInactiveUsers() {
        long inactivityThresholdMillis = 15 * 60 * 1000; // 15 minuta
        activeUserTracker.removeInactiveUsers(inactivityThresholdMillis);
    }
}
