package com.example.breadfeet_BE.domain.challenge;

import com.example.breadfeet_BE.auth.config.auth.dto.CustomOAuth2User;
import com.example.breadfeet_BE.domain.challenge.dto.AllMyChallengesResponseDto;
import com.example.breadfeet_BE.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/my")
    public ResponseEntity<AllMyChallengesResponseDto> getMyChallenges(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        User user = customOAuth2User.getUser();
        AllMyChallengesResponseDto myChallenges = challengeService.getAllMyChallenges(user);
        return ResponseEntity.ok(myChallenges);
    }
}
