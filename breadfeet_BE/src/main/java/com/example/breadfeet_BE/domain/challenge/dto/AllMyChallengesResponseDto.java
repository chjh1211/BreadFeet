package com.example.breadfeet_BE.domain.challenge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AllMyChallengesResponseDto {
    private List<ChallengeInfoResponseDto> recommended;
    private List<OngoingChallengeInfoResponseDto> ongoing;
    private List<ChallengeInfoResponseDto> completed;

    public AllMyChallengesResponseDto(List<ChallengeInfoResponseDto> recommended, List<OngoingChallengeInfoResponseDto> ongoing, List<ChallengeInfoResponseDto> completed) {
        this.recommended = recommended;
        this.ongoing = ongoing;
        this.completed = completed;
    }
}
