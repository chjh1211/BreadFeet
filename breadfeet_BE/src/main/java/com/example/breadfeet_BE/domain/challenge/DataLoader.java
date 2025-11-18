package com.example.breadfeet_BE.domain.challenge;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ChallengeRepository challengeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (challengeRepository.count() == 0) {
            // Region Challenges
            challengeRepository.save(new Challenge("대구 중구 초보", ChallengeType.REGION, "대구광역시 중구", null, 10, "대구 중구에서 10번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 중구 중수", ChallengeType.REGION, "대구광역시 중구", null, 100, "대구 중구에서 100번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 중구 고수", ChallengeType.REGION, "대구광역시 중구", null, 1000, "대구 중구에서 1000번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 수성구 초보", ChallengeType.REGION, "대구광역시 수성구", null, 10, "대구 수성구에서 10번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 수성구 중수", ChallengeType.REGION, "대구광역시 수성구", null, 100, "대구 수성구에서 100번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 수성구 고수", ChallengeType.REGION, "대구광역시 수성구", null, 1000, "대구 수성구에서 1000번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 동구 초보", ChallengeType.REGION, "대구광역시 동구", null, 10, "대구 동구에서 10번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 동구 중수", ChallengeType.REGION, "대구광역시 동구", null, 100, "대구 동구에서 100번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 동구 고수", ChallengeType.REGION, "대구광역시 동구", null, 1000, "대구 동구에서 1000번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 서구 초보", ChallengeType.REGION, "대구광역시 서구", null, 10, "대구 서구에서 10번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 서구 중수", ChallengeType.REGION, "대구광역시 서구", null, 100, "대구 서구에서 100번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 서구 고수", ChallengeType.REGION, "대구광역시 서구", null, 1000, "대구 서구에서 1000번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 남구 초보", ChallengeType.REGION, "대구광역시 남구", null, 10, "대구 남구에서 10번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 남구 중수", ChallengeType.REGION, "대구광역시 남구", null, 100, "대구 남구에서 100번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 남구 고수", ChallengeType.REGION, "대구광역시 남구", null, 1000, "대구 남구에서 1000번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 북구 초보", ChallengeType.REGION, "대구광역시 북구", null, 10, "대구 북구에서 10번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 북구 중수", ChallengeType.REGION, "대구광역시 북구", null, 100, "대구 북구에서 100번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 북구 고수", ChallengeType.REGION, "대구광역시 북구", null, 1000, "대구 북구에서 1000번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 달서구 초보", ChallengeType.REGION, "대구광역시 달서구", null, 10, "대구 달서구에서 10번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 달서구 중수", ChallengeType.REGION, "대구광역시 달서구", null, 100, "대구 달서구에서 100번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 달서구 고수", ChallengeType.REGION, "대구광역시 달서구", null, 1000, "대구 달서구에서 1000번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 달성군 초보", ChallengeType.REGION, "대구광역시 달성군", null, 10, "대구 달성군에서 10번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 달성군 중수", ChallengeType.REGION, "대구광역시 달성군", null, 100, "대구 달성군에서 100번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 달성군 고수", ChallengeType.REGION, "대구광역시 달성군", null, 1000, "대구 달성군에서 1000번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 군위군 초보", ChallengeType.REGION, "대구광역시 군위군", null, 10, "대구 군위군에서 10번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 군위군 중수", ChallengeType.REGION, "대구광역시 군위군", null, 100, "대구 군위군에서 100번 이상 리뷰 작성", "/img/badges/default.png"));
            challengeRepository.save(new Challenge("대구 군위군 고수", ChallengeType.REGION, "대구광역시 군위군", null, 1000, "대구 군위군에서 1000번 이상 리뷰 작성", "/img/badges/default.png"));

            // Frequency Challenges
            for (int month = 1; month <= 12; month++) {
                challengeRepository.save(new Challenge(
                        month + "월의 빵식이",
                        ChallengeType.FREQUENCY,
                        null,
                        month,
                        10,
                        month + "월 한 달 동안 10번 이상 리뷰 작성",
                        "/img/badges/default.png"
                ));
                challengeRepository.save(new Challenge(
                        month + "월의 빵생빵사",
                        ChallengeType.FREQUENCY,
                        null,
                        month,
                        100,
                        month + "월 한 달 동안 100번 이상 리뷰 작성",
                        "/img/badges/default.png"
                ));
            }
        }
    }
}
