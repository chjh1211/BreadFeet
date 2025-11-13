package com.example.breadfeet_BE.domain.challenge.dto;

import com.example.breadfeet_BE.domain.challenge.Challenge;
import com.example.breadfeet_BE.domain.challenge.ChallengeType;
import com.example.breadfeet_BE.domain.challenge.UserChallenge;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyChallengeResponseDto {
    private Long challengeId;
    private String challengeName;
    private ChallengeType challengeType;
    private String challengeRegion;
    private int challengeThreshold;
    private String challengeDescription;
    private LocalDateTime achievedAt;

    @Builder
    public MyChallengeResponseDto(UserChallenge userChallenge) {
        Challenge challenge = userChallenge.getChallenge();
        this.challengeId = challenge.getId();
        this.challengeName = challenge.getName();
        this.challengeType = challenge.getType();
        this.challengeRegion = challenge.getRegion();
        this.challengeThreshold = challenge.getThreshold();
        this.challengeDescription = challenge.getDescription();
        this.achievedAt = userChallenge.getAchievedAt();
    }
}
