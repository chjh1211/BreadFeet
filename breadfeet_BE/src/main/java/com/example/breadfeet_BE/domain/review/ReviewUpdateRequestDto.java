package com.example.breadfeet_BE.domain.review;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequestDto {
    private String content;
    private double rating;

    public ReviewUpdateRequestDto(String content, double rating) {
        this.content = content;
        this.rating = rating;
    }
}
