package com.example.breadfeet_BE.auth.config.auth.dto; // 본인 패키지명 확인

import com.example.breadfeet_BE.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;

    private String nickname;
    private String profileImageUrl;
    private String kakaoSocialId; // ⬅️ 'kakaoId'가 아니라 'kakaoSocialId'

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String nickname, String profileImageUrl, String kakaoSocialId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.kakaoSocialId = kakaoSocialId; // ⬅️ 여기!
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return null;
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        String id = attributes.get(userNameAttributeName).toString(); // 카카오 회원번호

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .nickname((String) profile.get("nickname"))
                .profileImageUrl((String) profile.get("profile_image_url"))
                .kakaoSocialId(id) // ⬅️ 'kakaoId(id)'가 아니라 'kakaoSocialId(id)'
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // User 엔티티의 Builder를 호출
    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .kakaoSocialId(kakaoSocialId) // ⬅️ 'kakaoId'가 아니라 'kakaoSocialId'
                .build();
    }
}