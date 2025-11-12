package com.example.breadfeet_BE.domain.challenge.dto;

import com.example.breadfeet_BE.domain.challenge.UserChallenge;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserChallengeResponseDto {
    private Long challengeId;
    private String challengeName;
    private String challengeDescription;
    private LocalDateTime achievedAt;

    public UserChallengeResponseDto(UserChallenge userChallenge) {
        this.challengeId = userChallenge.getChallenge().getId();
        this.challengeName = userChallenge.getChallenge().getName();
        this.challengeDescription = userChallenge.getChallenge().getDescription();
        this.achievedAt = userChallenge.getAchievedAt();
    }
}
