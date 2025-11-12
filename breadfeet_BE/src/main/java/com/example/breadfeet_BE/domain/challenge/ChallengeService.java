package com.example.breadfeet_BE.domain.challenge;

import com.example.breadfeet_BE.domain.bakery.Bakery;
import com.example.breadfeet_BE.domain.challenge.dto.UserChallengeResponseDto;
import com.example.breadfeet_BE.domain.review.ReviewRepository;
import com.example.breadfeet_BE.domain.user.User;
import com.example.breadfeet_BE.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    public List<UserChallengeResponseDto> getUserChallenges(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. id=" + userId));
        List<UserChallenge> userChallenges = userChallengeRepository.findAllByUser(user);
        return userChallenges.stream()
                .map(UserChallengeResponseDto::new)
                .collect(Collectors.toList());
    }
}
