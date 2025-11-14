package com.example.breadfeet_BE.auth.config.auth;

import com.example.breadfeet_BE.auth.config.auth.dto.CustomOAuth2User;
import com.example.breadfeet_BE.auth.config.auth.dto.OAuthAttributes;
import com.example.breadfeet_BE.auth.config.auth.dto.SessionUser;
import com.example.breadfeet_BE.domain.user.User;
import com.example.breadfeet_BE.domain.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService { // 1. extends 확인

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);

            log.info(oAuth2User.toString());

            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            String userNameAttributeName = userRequest.getClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUserNameAttributeName();

            OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

            User user = saveOrUpdate(attributes);

            httpSession.setAttribute("user", new SessionUser(user));

            return new CustomOAuth2User(
                    user,
                    oAuth2User.getAttributes()
            );

        } catch (Exception e) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!! [CustomOAuth2UserService] loadUser에서 에러 발생 !!!!!!!!!!!");
            e.printStackTrace(); // <-- 콘솔에 진짜 에러 스택을 출력
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            OAuth2Error error = new OAuth2Error("LOAD_USER_FAILED", "loadUser에서 에러가 발생했습니다.", null);
            throw new OAuth2AuthenticationException(error, e);
        }
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByKakaoSocialId(attributes.getKakaoSocialId())
                .map(entity -> entity.update(attributes.getNickname(), attributes.getProfileImageUrl()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}