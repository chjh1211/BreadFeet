package com.example.breadfeet_BE.auth.config.auth; // 본인 패키지명 확인

import com.example.breadfeet_BE.auth.config.auth.dto.OAuthAttributes;
import com.example.breadfeet_BE.auth.config.auth.dto.SessionUser;
import com.example.breadfeet_BE.auth.domain.user.User;
import com.example.breadfeet_BE.auth.domain.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
// import org.springframework.security.oauth2.core.OAuth2Error; // ⬅️ 이 import는 이제 필요 없습니다.
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    // ⬇️ ⬇️ ⬇️ 이 메서드를 통째로 교체하세요 ⬇️ ⬇️ ⬇️
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // ‼️‼️‼️ loadUser 전체를 try-catch로 감쌉니다 ‼️‼️‼️
        try {
            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String userNameAttributeName = userRequest.getClientRegistration()
                    .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

            // ‼️ (의심 지점 1) 여기서 에러가 날 수 있습니다.
            OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

            // ‼️ (의심 지점 2) 여기서 에러가 날 수 있습니다.
            User user = saveOrUpdate(attributes);

            // ‼️ (의심 지점 3) 여기서 에러가 날 수 있습니다.
            httpSession.setAttribute("user", new SessionUser(user));

            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole())),
                    attributes.getAttributes(),
                    attributes.getNameAttributeKey());

        } catch (Exception e) {
            // ‼️‼️‼️ 로그인 처리 중 어디선가 에러 발생 ‼️‼️‼️
            System.out.println("!!!!!!!!!!!!! OAuth2 로그인 처리 중 치명적 에러 !!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!! 에러 원인: " + e.getMessage());
            e.printStackTrace(); // ‼️‼️‼️ 콘솔에 빨간 스택 트레이스를 출력합니다.
            // ‼️‼️‼️ ‼️‼️‼️ ‼️‼️‼️ ‼️‼️‼️ ‼️‼️‼️

            // 로그인 과정을 중단시킵니다.
            throw e;
        }
    }

    // saveOrUpdate 메서드는 원래대로 둡니다 (try-catch 제거)
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByKakaoSocialId(attributes.getKakaoSocialId())
                .map(entity -> entity.update(attributes.getNickname(), attributes.getProfileImageUrl()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}