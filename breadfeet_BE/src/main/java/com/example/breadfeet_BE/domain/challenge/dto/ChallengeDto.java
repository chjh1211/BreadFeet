package com.example.breadfeet_BE.domain.challenge.dto;

import com.example.breadfeet_BE.domain.challenge.Challenge;
import com.example.breadfeet_BE.domain.challenge.UserChallenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeDto {
    private Long id;
    private String title;
    private String img;
    private String body;
    private Integer progress; // Only for ongoing challenges
    private Integer total;    // Only for ongoing challenges

    @Builder
    public ChallengeDto(Long id, String title, String img, String body, Integer progress, Integer total) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.body = body;
        this.progress = progress;
        this.total = total;
    }

    // For completed/recommended challenges
    public ChallengeDto(Challenge challenge) {
        this.id = challenge.getId();
        this.title = challenge.getName();
        this.img = challenge.getImgUrl();
        this.body = challenge.getDescription();
        this.progress = null;
        this.total = null;
    }

    // For ongoing challenges
    public ChallengeDto(UserChallenge userChallenge, int progress) {
        Challenge challenge = userChallenge.getChallenge();
        this.id = challenge.getId();
        this.title = challenge.getName();
        this.img = challenge.getImgUrl();
        this.body = challenge.getDescription();
        this.progress = progress;
        this.total = challenge.getThreshold();
    }
}
