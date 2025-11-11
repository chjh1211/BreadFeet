package com.example.breadfeet_BE.security;

import com.example.breadfeet_BE.auth.config.auth.dto.CustomOAuth2User;
import com.example.breadfeet_BE.domain.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = User.builder()
                .kakaoSocialId(customUser.kakaoSocialId())
                .nickname(customUser.nickname())
                .profileImageUrl(customUser.profileImageUrl())
                .build();

        CustomOAuth2User principal = new CustomOAuth2User(user, Collections.emptyMap());

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
