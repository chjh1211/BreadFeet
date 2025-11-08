package com.example.breadfeet_BE.auth.config.auth.dto;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 1. 모든 경로(/**)에 대해
                .allowedOrigins("http://localhost:5173") // 2. 5173 포트의 요청을 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS") // 3. 허용할 HTTP 메서드
                .allowCredentials(true) // 4. (선택) 쿠키/세션 인증이 필요하면 true
                .maxAge(3600); // (선택)
    }
}