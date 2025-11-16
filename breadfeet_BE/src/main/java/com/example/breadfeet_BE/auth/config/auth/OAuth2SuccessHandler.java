package com.example.breadfeet_BE.auth.config.auth;

import com.example.breadfeet_BE.auth.config.auth.dto.CustomOAuth2User;
import com.example.breadfeet_BE.domain.user.Role;
import com.example.breadfeet_BE.domain.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String REDIRECT_URL = "http://localhost:5173/login/success";


    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("onAuthenticationSuccess: Authentication successful. Processing to create and set JWT cookie.");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        String userIdentifier = user.getId().toString();
        Role role = oAuth2User.getRole();

        String token = jwtTokenProvider.createToken(userIdentifier, role);

        Cookie cookie = createCookie(request, JwtTokenProvider.COOKIE_NAME, token);
        response.addCookie(cookie);

        String redirectUrl = UriComponentsBuilder.fromUriString(REDIRECT_URL)
                .build().toUriString();

        log.info("Redirecting to: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    private Cookie createCookie(HttpServletRequest request, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(false);
        // localhost에서 테스트할 경우 Secure 속성은 false여야 함
        if (!request.getServerName().equalsIgnoreCase("localhost")) {
            cookie.setSecure(true);
        }
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1시간
        // SameSite 설정을 추가할 수 있습니다. 예: cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }
}