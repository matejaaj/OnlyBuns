package com.example.onlybunsbe.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ActiveUsersService {
    private final AtomicInteger activeUsers = new AtomicInteger(0);

    public ActiveUsersService(MeterRegistry meterRegistry) {
        meterRegistry.gauge("app.active.users", activeUsers);
    }

    public void userLoggedIn() {
        activeUsers.incrementAndGet();
    }

    public void userLoggedOut() {
        activeUsers.decrementAndGet();
    }

    public int getActiveUsers() {
        return activeUsers.get();
    }
}
