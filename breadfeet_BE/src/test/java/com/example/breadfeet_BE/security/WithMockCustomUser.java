package com.example.breadfeet_BE.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String kakaoSocialId() default "test-kakao-id";
    String nickname() default "test-user";
    String profileImageUrl() default "http://test.com/image.jpg";
}
