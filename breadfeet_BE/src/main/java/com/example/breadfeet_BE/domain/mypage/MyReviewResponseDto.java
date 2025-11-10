package com.example.breadfeet_BE.domain.mypage;

import com.example.breadfeet_BE.domain.review.Review;
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

    public MyReviewResponseDto(Review review) {
        this.reviewId = review.getReviewid();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.createdAt = review.getCreatedAt();
        this.bakeryId = review.getBakery().getId();
        this.bakeryName = review.getBakery().getName();
    }
}
