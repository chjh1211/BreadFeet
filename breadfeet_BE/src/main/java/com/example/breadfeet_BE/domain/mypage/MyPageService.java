package com.example.breadfeet_BE.domain.mypage;

import com.example.breadfeet_BE.domain.user.User;
import com.example.breadfeet_BE.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public MyPageResponseDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return new MyPageResponseDto(user);
    }
}
