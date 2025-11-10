package com.example.breadfeet_BE.domain.review;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewCreateRequestDto {
    private String content;
    private double rating;
}
