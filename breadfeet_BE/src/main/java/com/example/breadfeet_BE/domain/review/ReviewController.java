package com.example.breadfeet_BE.domain.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/api/bakeries/{bakeryId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByBakery(
            @PathVariable Long bakeryId
    )
    {
        List<ReviewResponseDto> reviewList = reviewService.findReviewsByBakeryId(bakeryId);
        // (참고) 명세서의 totalReviews, avgRating은
        // reviewList.size()와 별도 계산 로직을 Service에 추가해야 합니다.
        return ResponseEntity.ok(reviewList);
    }
}
