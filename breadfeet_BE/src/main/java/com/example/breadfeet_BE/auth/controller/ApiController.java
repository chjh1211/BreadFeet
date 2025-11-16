package com.example.breadfeet_BE.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/recommended")
    public ResponseEntity<?> getRecommendedBakeries() {
        // 나중에는 실제 서비스 로직으로 대체
        // 지금은 빈 리스트(empty list)를 반환
        return ResponseEntity.ok(Collections.emptyList());
    }
    @GetMapping("/topBakeries")
    public ResponseEntity<?> getTopBakeries() {
        // 지금은 빈 리스트(empty list)를 반환
        return ResponseEntity.ok(Collections.emptyList());
    }
}
