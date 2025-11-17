package com.example.breadfeet_BE.domain.challenge;

import com.example.breadfeet_BE.domain.bakery.Bakery;
import com.example.breadfeet_BE.domain.challenge.dto.AllMyChallengesResponseDto;
import com.example.breadfeet_BE.domain.challenge.dto.ChallengeInfoResponseDto;
import com.example.breadfeet_BE.domain.challenge.dto.OngoingChallengeInfoResponseDto;
import com.example.breadfeet_BE.domain.review.ReviewRepository;
import com.example.breadfeet_BE.domain.user.User;
import com.example.breadfeet_BE.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public void updateChallengeProgress(User user, Bakery bakery) {
        checkRegionChallenges(user, bakery);
        checkFrequencyChallenges(user);
    }

    private void checkRegionChallenges(User user, Bakery bakery) {
        String region = bakery.getCity() + " " + bakery.getDistrict();
        long reviewCount = reviewRepository.countByUserAndBakery_CityAndBakery_District(user, bakery.getCity(), bakery.getDistrict());

        List<Challenge> regionChallenges = challengeRepository.findByType(ChallengeType.REGION);
        for (Challenge challenge : regionChallenges) {
            if (challenge.getRegion().equals(region) && reviewCount >= challenge.getThreshold()) {
                awardChallenge(user, challenge);
            }
        }
    }

    private void checkFrequencyChallenges(User user) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        long reviewCount = reviewRepository.countByUserAndCreatedAtAfter(user, thirtyDaysAgo);

        List<Challenge> frequencyChallenges = challengeRepository.findByType(ChallengeType.FREQUENCY);
        for (Challenge challenge : frequencyChallenges) {
            if (reviewCount >= challenge.getThreshold()) {
                awardChallenge(user, challenge);
            }
        }
    }

    private void awardChallenge(User user, Challenge challenge) {
        // 이미 획득한 챌린지인지 확인
        if (userChallengeRepository.findByUserAndChallenge(user, challenge).isEmpty()) {
            UserChallenge userChallenge = new UserChallenge(user, challenge);
            userChallengeRepository.save(userChallenge);
        }
    }

    @Transactional(readOnly = true)
    public AllMyChallengesResponseDto getAllMyChallenges(User user) {
        // 1. 'completed' 챌린지 조회
        List<UserChallenge> completedUserChallenges = userChallengeRepository.findAllByUser(user);
        List<Challenge> completedChallenges = completedUserChallenges.stream()
                .map(UserChallenge::getChallenge)
                .toList();
        List<ChallengeInfoResponseDto> completedDto = completedChallenges.stream()
                .map(ChallengeInfoResponseDto::new)
                .toList();

        Set<Long> completedChallengeIds = completedChallenges.stream()
                .map(Challenge::getId)
                .collect(Collectors.toSet());

        // 2. 'ongoing' 및 'recommended' 챌린지 후보 조회 (완료되지 않은 모든 챌린지)
        List<Challenge> allOtherChallenges = challengeRepository.findAll().stream()
                .filter(c -> !completedChallengeIds.contains(c.getId()))
                .toList();

        List<OngoingChallengeInfoResponseDto> ongoingDto = new java.util.ArrayList<>();
        List<ChallengeInfoResponseDto> recommendedDto = new java.util.ArrayList<>();

        // 3. 진행도에 따라 'ongoing'과 'recommended'로 분류
        for (Challenge challenge : allOtherChallenges) {
            int progress = calculateProgress(user, challenge);
            if (progress > 0) {
                ongoingDto.add(new OngoingChallengeInfoResponseDto(challenge, progress));
            } else {
                recommendedDto.add(new ChallengeInfoResponseDto(challenge));
            }
        }

        // 4. 최종 DTO 조립 및 반환
        return new AllMyChallengesResponseDto(recommendedDto, ongoingDto, completedDto);
    }

    private int calculateProgress(User user, Challenge challenge) {
        if (challenge.getType() == ChallengeType.REGION) {
            String region = challenge.getRegion();
            String[] parts = region.split(" ");
            String city = parts[0];
            String district = parts[1];
            return (int) reviewRepository.countByUserAndBakery_CityAndBakery_District(user, city, district);
        } else if (challenge.getType() == ChallengeType.FREQUENCY) {
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
            return (int) reviewRepository.countByUserAndCreatedAtAfter(user, thirtyDaysAgo);
        }
        return 0;
    }


}
