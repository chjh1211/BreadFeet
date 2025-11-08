package com.example.breadfeet_BE.auth.config.auth;

import com.example.breadfeet_BE.auth.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Spring Security 설정 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // csrf 비활성화 (테스트용)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // h2-console 사용 시

                // 1. URL별 권한 관리
                .authorizeHttpRequests(authz -> authz
                        // 다음 URL들은 모두 허용
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**","/testdb").permitAll()
                        // "/api/v1/**" 주소를 가진 API는 "USER" 권한을 가진 사람만 가능
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                        // 그 외 모든 요청은 인증된 사용자만 가능
                        .anyRequest().authenticated()
                )

                // 2. 로그아웃 설정
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 / (루트)로 이동
                )

                // 3. OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        // 로그인 성공 후 사용자 정보를 가져올 때의 설정
                        .userInfoEndpoint(userInfo -> userInfo
                                // (중요) 소셜 로그인 성공 시 후속 조치를 진행할 서비스 등록
                                .userService(customOAuth2UserService)
                        )
                );

        return http.build();
    }
}