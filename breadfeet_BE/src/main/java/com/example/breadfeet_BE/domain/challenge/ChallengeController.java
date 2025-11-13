package com.example.breadfeet_BE.domain.challenge;

import com.example.breadfeet_BE.auth.config.auth.dto.CustomOAuth2User;
import com.example.breadfeet_BE.domain.challenge.ChallengeService;
import com.example.breadfeet_BE.domain.challenge.dto.MyChallengeResponseDto;
import com.example.breadfeet_BE.domain.challenge.dto.UserChallengeResponseDto;
import com.example.breadfeet_BE.domain.user.User;
import com.example.breadfeet_BE.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final UserRepository userRepository;

    @GetMapping("/my")
    public ResponseEntity<List<UserChallengeResponseDto>> getMyChallenges() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = customOAuth2User.getUser();

        List<UserChallengeResponseDto> myChallenges = challengeService.getUserChallenges(user.getId());
        return ResponseEntity.ok(myChallenges);
    }

    @GetMapping("/my-achieved")
    public ResponseEntity<List<MyChallengeResponseDto>> getMyAchievedChallenges() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = customOAuth2User.getUser();

        List<MyChallengeResponseDto> myAchievedChallenges = challengeService.getMyAchievedChallenges(user);
        return ResponseEntity.ok(myAchievedChallenges);
    }
}
