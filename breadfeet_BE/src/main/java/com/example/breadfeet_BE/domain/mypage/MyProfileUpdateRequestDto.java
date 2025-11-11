package com.example.breadfeet_BE.domain.mypage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyProfileUpdateRequestDto {
    private String nickname;
    private String profileImageUrl;

    // 추후 빵 취향 관련 필드가 추가될 수 있습니다.
    // private String breadPreference;
}
