package com.example.breadfeet_BE.domain.bakery;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bakeries")
public class BakeryContoller {
    private final BakeryService bakeryService;

    @GetMapping
    public ResponseEntity<List<BakeryListResponseDto>> getBakeryList(@RequestParam(required = false) String search) {
        List<BakeryListResponseDto> bakeryList = bakeryService.findAllBakeries(search);

        return ResponseEntity.ok(bakeryList);
    }

    @GetMapping("/{bakeryId}")
    public ResponseEntity<BakeryDetailResponseDto> getBakeryDetail(@PathVariable Long bakeryId){
        BakeryDetailResponseDto bakeryDetail = bakeryService.findBakeryById(bakeryId);
        return ResponseEntity.ok(bakeryDetail);
    }
}
