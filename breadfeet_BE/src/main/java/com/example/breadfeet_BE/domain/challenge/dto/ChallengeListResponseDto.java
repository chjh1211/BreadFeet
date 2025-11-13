package com.example.breadfeet_BE.domain.challenge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengeListResponseDto {
    private List<ChallengeDto> completed;
    private List<ChallengeDto> ongoing;
    private List<ChallengeDto> recommended;

    @Builder
    public ChallengeListResponseDto(List<ChallengeDto> completed, List<ChallengeDto> ongoing, List<ChallengeDto> recommended) {
        this.completed = completed;
        this.ongoing = ongoing;
        this.recommended = recommended;
    }
}
