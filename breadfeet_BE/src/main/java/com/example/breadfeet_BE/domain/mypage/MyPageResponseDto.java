package com.example.breadfeet_BE.domain.mypage;

import com.example.breadfeet_BE.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPageResponseDto {
    private String nickname;
    private String profileImageUrl;

    // 추후 빵 취향 관련 필드가 추가될 수 있습니다.
    // private String breadPreference;

    public MyPageResponseDto(User user) {
        this.nickname = user.getNickname();
        this.profileImageUrl = user.getProfileImageUrl();
    }
}
