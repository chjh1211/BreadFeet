package com.example.breadfeet_BE.auth.config.auth.dto;

import com.example.breadfeet_BE.auth.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String nickname;
    private String profileImageUrl;
    private String role;

    public SessionUser(User user) {
        this.nickname = user.getNickname();
        this.profileImageUrl = user.getProfileImageUrl();
        this.role = user.getRole();
    }
}