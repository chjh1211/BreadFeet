package com.example.breadfeet_BE.domain.bakery;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bakeries")
public class BakeryContoller {
    private final BakeryService bakeryService;

    @GetMapping
    public ResponseEntity<List<BakeryListResponseDto>> getBakeryList() {
        List<BakeryListResponseDto> bakeryList = bakeryService.findAllBakeries();

        return ResponseEntity.ok(bakeryList);
    }
}
