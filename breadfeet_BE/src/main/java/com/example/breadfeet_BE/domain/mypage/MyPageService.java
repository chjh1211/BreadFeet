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

    @Transactional
    public MyPageResponseDto updateMyProfile(Long userId, MyProfileUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        user.update(requestDto.getNickname(), requestDto.getProfileImageUrl());
        // user 객체는 영속성 컨텍스트에 의해 관리되므로, 별도의 save 호출 없이 변경 사항이 DB에 반영됩니다.
        // 하지만 명시적인 save를 원한다면 userRepository.save(user); 를 호출할 수 있습니다.

        return new MyPageResponseDto(user);
    }
}
