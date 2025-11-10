package com.example.breadfeet_BE.auth.config.auth.dto;

import com.example.breadfeet_BE.domain.user.Role;
import com.example.breadfeet_BE.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * ✅ OAuth2 로그인 이후 인증된 사용자 정보를 담는 클래스
 */
@Getter
public class CustomOAuth2User implements OAuth2User, UserDetails {

    private final User user;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // ▼▼▼ [수정됨] user.getEmail() 대신 user.getNickname() 반환 ▼▼▼
    public String getEmail() {
        return user.getNickname();
    }

    public Role getRole() {
        return user.getRole(); // ⬅️ User.java가 수정되어 이제 정상 동작
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getKey()));
    }

    // ▼▼▼ [수정됨] user.getEmail() 대신 user.getNickname() 반환 ▼▼▼
    @Override
    public String getName() {
        return user.getNickname();
    }

    // ===== UserDetails 인터페이스 구현부 (JWT, 세션 통합용) =====
    @Override
    public String getPassword() {
        return null;
    }

    // ▼▼▼ [수정됨] user.getEmail() 대신 user.getNickname() 반환 ▼▼▼
    @Override
    public String getUsername() {
        return user.getNickname(); // ⬅️ UserDetails의 username으로 닉네임 사용
    }

    // ... (isAccountNonExpired 등 나머지 메서드는 그대로) ...
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