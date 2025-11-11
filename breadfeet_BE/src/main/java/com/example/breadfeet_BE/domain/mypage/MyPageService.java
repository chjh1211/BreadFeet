package com.example.breadfeet_BE.domain.mypage;

import com.example.breadfeet_BE.domain.mypage.MyPageResponseDto;
import com.example.breadfeet_BE.domain.mypage.MyProfileUpdateRequestDto;
import com.example.breadfeet_BE.domain.mypage.MyReviewResponseDto;
import com.example.breadfeet_BE.domain.review.ReviewRepository;
import com.example.breadfeet_BE.domain.user.User;
import com.example.breadfeet_BE.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public MyPageResponseDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return new MyPageResponseDto(user);
    }

    @Transactional
    public MyPageResponseDto updateMyProfile(Long userId, MyProfileUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        user.update(requestDto.getNickname(), requestDto.getProfileImageUrl());
        return new MyPageResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<MyReviewResponseDto> getMyReviews(Long userId) {
        // Check if user exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        return reviewRepository.findByUser_Id(userId).stream()
                .map(MyReviewResponseDto::new)
                .collect(Collectors.toList());
    }
}
