package com.example.breadfeet_BE.auth.config.auth;

import com.example.breadfeet_BE.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity // Spring Security 설정 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))

                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**","/testdb").permitAll()
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                )

                // 3. OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        // ▼▼▼ 이 줄을 여기에 추가하세요! ▼▼▼
                        .successHandler(oauthSuccessHandler()) // 만든 핸들러를 등록
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler oauthSuccessHandler() {
        return (request, response, authentication) -> {
            // 로그인이 성공했으므로, 프론트엔드 메인 페이지로 리다이렉트
            response.sendRedirect("http://localhost:5175");
        };
    }
}