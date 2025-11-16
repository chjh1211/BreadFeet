package com.example.breadfeet_BE.domain.user;

import com.example.breadfeet_BE.global.CustomRuntimeException;
import com.example.breadfeet_BE.global.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final UserRepository userRepository;

    public UserResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.DO_NOT_EXIST_USER));
        return new UserResponseDto(user);
    }
}
