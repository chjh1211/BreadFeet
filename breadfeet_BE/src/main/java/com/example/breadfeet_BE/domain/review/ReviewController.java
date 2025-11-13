package com.example.breadfeet_BE.domain.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/api/reviews/{bakeryId}/reviews") // ⬅️ API 명세서 4번
    public ResponseEntity<ReviewCreateResponseDto> createReview(

            @PathVariable Long bakeryId,
            // 1. @RequestBody: 프론트가 보낸 JSON을 DTO로 변환
            @RequestBody ReviewCreateRequestDto requestDto,

            // 2. @AuthenticationPrincipal:
            //    JwtAuthenticationFilter가 SecurityContext에 저장한
            //    "사용자 ID(String)"를 여기에 주입해 줍니다.
            @AuthenticationPrincipal String userId
    ) {
        // 3. 서비스에게 DTO와 사용자 ID를 넘겨 리뷰 생성 요청
        Long newReviewId = reviewService.createReview(requestDto, userId,bakeryId);

        // 4. 성공 응답 (HTTP 201 Created)과 함께 생성된 ID를 DTO에 담아 반환
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ReviewCreateResponseDto(newReviewId));
    }

    @PutMapping("/api/reviews/{reviewId}")
    public ResponseEntity<String> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequestDto requestDto,
            @AuthenticationPrincipal String userId
    ) {
        reviewService.updateReview(reviewId, requestDto, userId);
        return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
    }
}
