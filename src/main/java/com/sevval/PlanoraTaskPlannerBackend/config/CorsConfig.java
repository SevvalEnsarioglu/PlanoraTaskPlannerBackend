package com.sevval.PlanoraTaskPlannerBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/v1/**") // Tüm API isteklerine izin ver
                        .allowedOrigins("*") // React Native veya Web tarafından gelen isteklere (şimdilik) full izin veriliyor. Prod ortamında spesifik origin girilmeli.
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .maxAge(3600); // 1 saat cache
            }
        };
    }
}
