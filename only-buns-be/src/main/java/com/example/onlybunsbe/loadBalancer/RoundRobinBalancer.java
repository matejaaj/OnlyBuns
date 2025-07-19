package com.example.onlybunsbe.loadBalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinBalancer {

    private final List<String> instances;
    private final AtomicInteger counter = new AtomicInteger(0);

    public RoundRobinBalancer(List<String> instances) {
        this.instances = instances;
    }

    public String getNextInstance() {
        int index = counter.getAndUpdate(i -> (i + 1) % instances.size());
        return instances.get(index);
    }
}
