package com.example.breadfeet_BE.domain.bakery;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bakeries")
public class BakeryContoller {
    private final BakeryService bakeryService;

    @GetMapping
    public ResponseEntity<BakeryPageResponseDto> getBakeryList(@RequestParam(required = false) String search, Pageable pageable) {
        BakeryPageResponseDto bakeryList = bakeryService.findAllBakeries(search, pageable);

        return ResponseEntity.ok(bakeryList);
    }

    @GetMapping("/{bakeryId}")
    public ResponseEntity<BakeryDetailResponseDto> getBakeryDetail(@PathVariable Long bakeryId){
        BakeryDetailResponseDto bakeryDetail = bakeryService.findBakeryById(bakeryId);
        return ResponseEntity.ok(bakeryDetail);
    }
}
