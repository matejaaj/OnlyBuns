package com.example.onlybunsbe.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200"); // Dozvoljava origin za Angular frontend
        config.addAllowedHeader("*"); // Dozvoljava sve zaglavlja
        config.addAllowedMethod("*"); // Dozvoljava sve HTTP metode (GET, POST, itd.)

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
