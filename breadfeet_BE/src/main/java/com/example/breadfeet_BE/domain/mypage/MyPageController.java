package com.example.breadfeet_BE.domain.mypage;

import com.example.breadfeet_BE.auth.config.auth.dto.CustomOAuth2User;
import com.example.breadfeet_BE.domain.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping; // Add this import
import org.springframework.web.bind.annotation.RequestBody; // Add this import
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/profile")
    public ResponseEntity<MyPageResponseDto> getMyProfile(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        // customOAuth2User.getUser().getId()를 통해 User 엔티티의 ID를 가져옵니다.
        Long userId = customOAuth2User.getUser().getId();
        MyPageResponseDto myProfile = myPageService.getMyProfile(userId);
        return ResponseEntity.ok(myProfile);
    }

    @PatchMapping("/profile")
    public ResponseEntity<MyPageResponseDto> updateMyProfile(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody MyProfileUpdateRequestDto requestDto) {
        Long userId = customOAuth2User.getUser().getId();
        MyPageResponseDto updatedProfile = myPageService.updateMyProfile(userId, requestDto);
        return ResponseEntity.ok(updatedProfile);
    }
}