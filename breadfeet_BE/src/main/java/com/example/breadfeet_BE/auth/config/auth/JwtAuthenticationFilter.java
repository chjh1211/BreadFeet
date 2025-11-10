package com.example.breadfeet_BE.auth.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component // 1. 이 필터를 Spring Bean으로 등록
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Request 헤더에서 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);

        // 2. 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 3. 토큰이 유효하면 인증 정보(Authentication) 객체 생성
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // 4. SecurityContextHolder에 인증 정보 저장 (로그인 상태로 만듦)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}