package com.example.breadfeet_BE.domain.review;

import jakarta.persistence.Column;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    private Long reviewid;
    private String content;
    private double rating;
    private String authorNickname;
    private LocalDateTime createdAt;
    private int likesCount;
    private int dislikesCount;

    public ReviewResponseDto(Review entity) {
        this.reviewid = entity.getId();
        this.content = entity.getContent();
        this.rating = entity.getRating();
        this.authorNickname = entity.getUser().getNickname();
        this.createdAt = entity.getCreatedAt();
        this.likesCount = entity.getLikesCount();
        this.dislikesCount = entity.getDislikesCount();
    }
}
