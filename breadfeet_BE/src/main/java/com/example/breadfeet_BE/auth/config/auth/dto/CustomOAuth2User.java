package com.example.breadfeet_BE.auth.config.auth.dto;

import com.example.breadfeet_BE.domain.user.Role;
import com.example.breadfeet_BE.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * ✅ OAuth2 로그인 이후 인증된 사용자 정보를 담는 클래스
 * - Spring Security의 Principal 역할 (SecurityContextHolder에 저장됨)
 * - DB의 User 엔티티와 연결되어 JWT 발급 시 핵심 정보 제공
 */
public class CustomOAuth2User implements OAuth2User, UserDetails {

    private final User user; // DB의 User 엔티티
    private final Map<String, Object> attributes; // 카카오에서 내려온 원본 데이터

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public Role getRole() {
        return user.getRole();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // Spring Security 권한 반환 (ex. ROLE_USER)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getKey()));
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

    // ===== UserDetails 인터페이스 구현부 (JWT, 세션 통합용) =====
    @Override
    public String getPassword() {
        return null; // OAuth 로그인은 비밀번호 사용 안 함
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
