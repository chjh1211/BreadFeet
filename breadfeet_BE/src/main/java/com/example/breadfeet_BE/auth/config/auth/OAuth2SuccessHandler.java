package com.example.breadfeet_BE.auth.config.auth;

import com.example.breadfeet_BE.auth.config.auth.dto.CustomOAuth2User;
import com.example.breadfeet_BE.domain.user.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("onAuthenticationSuccess");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // ▼▼▼ [수정됨] getEmail() 대신 getUsername() (닉네임) 사용 ▼▼▼
        String userIdentifier = oAuth2User.getUsername(); // ⬅️ 닉네임
        Role role = oAuth2User.getRole();

        // ▼▼▼ [수정됨] 이메일 대신 닉네임(userIdentifier)으로 토큰 생성 ▼▼▼
        String token = jwtTokenProvider.createToken(userIdentifier, role);

        String redirectUrl = "http://localhost:5175/login/success?token=" +
                URLEncoder.encode(token, StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}