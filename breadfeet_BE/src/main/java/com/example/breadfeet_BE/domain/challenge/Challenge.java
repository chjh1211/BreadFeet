package com.example.breadfeet_BE.domain.challenge;

import com.example.breadfeet_BE.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "challenge")
public class Challenge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @Column(nullable = false)
    private String name; // 챌린지 이름 (ex. 중구 초보)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeType type; // 챌린지 타입 (REGION, FREQUENCY)

    private String region; // 챌린지 지역 (ex. 대구광역시 중구), type이 REGION일 경우에만 값 존재

    @Column(nullable = false)
    private int threshold; // 챌린지 달성 조건 (리뷰 작성 횟수)

    @Column(nullable = false)
    private String description; // 챌린지 설명

    public Challenge(String name, ChallengeType type, String region, int threshold, String description) {
        this.name = name;
        this.type = type;
        this.region = region;
        this.threshold = threshold;
        this.description = description;
    }
}
