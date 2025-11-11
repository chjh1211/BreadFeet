package com.example.breadfeet_BE.domain.mypage;

import com.example.breadfeet_BE.auth.config.auth.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// All DTOs and the Service need to be imported
import com.example.breadfeet_BE.domain.mypage.MyPageService;
import com.example.breadfeet_BE.domain.mypage.MyPageResponseDto;
import com.example.breadfeet_BE.domain.mypage.MyProfileUpdateRequestDto;
import com.example.breadfeet_BE.domain.mypage.MyReviewResponseDto;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/profile")
    public ResponseEntity<MyPageResponseDto> getMyProfile(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
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

    @GetMapping("/reviews")
    public ResponseEntity<List<MyReviewResponseDto>> getMyReviews(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getUser().getId();
        List<MyReviewResponseDto> myReviews = myPageService.getMyReviews(userId);
        return ResponseEntity.ok(myReviews);
    }
}