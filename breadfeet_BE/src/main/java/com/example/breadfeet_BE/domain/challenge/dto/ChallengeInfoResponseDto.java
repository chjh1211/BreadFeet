package com.example.breadfeet_BE.domain.challenge.dto;

import com.example.breadfeet_BE.domain.challenge.Challenge;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeInfoResponseDto {
    private Long id;
    private String title;
    private String img;
    private String body;

    public ChallengeInfoResponseDto(Challenge challenge) {
        this.id = challenge.getId();
        this.title = challenge.getName();
        this.img = challenge.getImgUrl();
        this.body = challenge.getDescription();
    }
}
