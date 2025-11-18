package com.example.breadfeet_BE.domain.challenge.dto;

import com.example.breadfeet_BE.domain.challenge.Challenge;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OngoingChallengeInfoResponseDto {
    private Long id;
    private String title;
    private String img;
    private String body;
    private int progress;
    private int total;

    public OngoingChallengeInfoResponseDto(Challenge challenge, int progress) {
        this.id = challenge.getId();
        this.title = challenge.getName();
        this.img = challenge.getImgUrl();
        this.body = challenge.getDescription();
        this.progress = progress;
        this.total = challenge.getThreshold();
    }
}
