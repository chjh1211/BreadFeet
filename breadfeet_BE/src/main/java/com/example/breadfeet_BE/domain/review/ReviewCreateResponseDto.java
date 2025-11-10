package com.example.breadfeet_BE.domain.review;

import lombok.Getter;

@Getter
public class ReviewCreateResponseDto {
    private Long reviewId;

    public ReviewCreateResponseDto(Long reviewId) {
        this.reviewId = reviewId;
    }
}
