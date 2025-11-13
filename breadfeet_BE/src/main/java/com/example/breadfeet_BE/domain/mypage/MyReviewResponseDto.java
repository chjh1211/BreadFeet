package com.example.breadfeet_BE.domain.mypage;

import com.example.breadfeet_BE.domain.review.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MyReviewResponseDto {
    private Long reviewId;
    private String content;
    private double rating;
    private LocalDateTime createdAt;
    private Long bakeryId;
    private String bakeryName;

    // Existing constructor
    public MyReviewResponseDto(Review review) {
        this.reviewId = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.createdAt = review.getCreatedAt();
        this.bakeryId = review.getBakery().getId();
        this.bakeryName = review.getBakery().getName();
    }

    // Add this builder constructor for easier testing
    @Builder
    public MyReviewResponseDto(Long reviewId, String content, double rating, LocalDateTime createdAt, Long bakeryId, String bakeryName) {
        this.reviewId = reviewId;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
        this.bakeryId = bakeryId;
        this.bakeryName = bakeryName;
    }
}
