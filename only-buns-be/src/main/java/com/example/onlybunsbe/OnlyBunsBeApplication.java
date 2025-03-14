package com.example.onlybunsbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class OnlyBunsBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlyBunsBeApplication.class, args);
    }

}
