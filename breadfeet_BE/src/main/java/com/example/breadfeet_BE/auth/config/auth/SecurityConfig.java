package com.example.breadfeet_BE.auth.config.auth; // 본인 패키지명 확인

import com.example.breadfeet_BE.domain.user.Role;
import lombok.RequiredArgsConstructor; // 1. @RequiredArgsConstructor import
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.AuthenticationSuccessHandler; // 5. 이 import는 이제 필요 없습니다.

@Configuration
@EnableWebSecurity // Spring Security 설정 활성화
@RequiredArgsConstructor // 2. final 필드 주입을 위해 추가
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler; // 3. JWT 성공 핸들러 주입

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
                        // ▼▼▼ 4. 주입받은 JWT 핸들러(oAuth2SuccessHandler)로 교체 ▼▼▼
                        .successHandler(oAuth2SuccessHandler)
                );

        return http.build();
    }

    // 5. 내부에 직접 정의했던 @Bean 핸들러는 삭제합니다.
    /*
    @Bean
    public AuthenticationSuccessHandler oauthSuccessHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect("http://localhost:5175");
        };
    }
    */
}