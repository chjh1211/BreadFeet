package com.example.breadfeet_BE.domain.mypage;

import com.example.breadfeet_BE.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

// import java.util.List; // badgeList를 위한 import (필요시 주석 해제)

@Getter
@NoArgsConstructor
public class MyPageResponseDto {
    private String nickname;
    private String profileImageUrl;

    // 추후 빵 취향 관련 필드가 추가될 수 있습니다.
    // private String breadPreference;

    // 추후 추가될 필드들 (요청에 따라 주석 처리)
    // private String region;
    // private List<String> badgeList; // 예시: 뱃지 이름 목록

    public MyPageResponseDto(User user) {
        this.nickname = user.getNickname();
        this.profileImageUrl = user.getProfileImageUrl();
        // this.region = user.getRegion(); // User 엔티티에 region 필드 추가 후 주석 해제
        // this.badgeList = user.getBadgeList(); // User 엔티티에 badgeList 필드 추가 후 주석 해제
    }
}
