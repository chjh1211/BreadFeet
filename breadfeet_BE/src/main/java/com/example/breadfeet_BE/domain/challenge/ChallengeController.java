package com.example.breadfeet_BE.domain.challenge;

import com.example.breadfeet_BE.domain.challenge.dto.UserChallengeResponseDto;
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

    @GetMapping("/my")
    public ResponseEntity<List<UserChallengeResponseDto>> getMyChallenges() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        List<UserChallengeResponseDto> myChallenges = challengeService.getUserChallenges(userId);
        return ResponseEntity.ok(myChallenges);
    }
}
