package com.example.breadfeet_BE.auth.config.auth;

import com.example.breadfeet_BE.auth.config.auth.dto.CustomOAuth2User;
import com.example.breadfeet_BE.domain.user.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        // ✅ 로그인 성공한 사용자 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 이메일과 역할 정보 추출
        String email = oAuth2User.getEmail();
        Role role = oAuth2User.getRole();

        // ✅ JWT 토큰 생성 (Role.name() 대신 key 값 사용하면 ROLE_ 접두어 유지됨)
        String token = jwtTokenProvider.createToken(email, role);

        // ✅ 프론트엔드로 리다이렉트 (소문자 localhost + UTF-8 인코딩)
        String redirectUrl = "http://localhost:5175/login/success?token=" +
                URLEncoder.encode(token, StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}
