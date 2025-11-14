package com.example.breadfeet_BE.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다."),

    // 유저 관련 에러
    DO_NOT_EXIST_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없음"),
    //빵집 관련 에러
    DO_NOT_EXIST_BAKERY(HttpStatus.NOT_FOUND, "빵집 못찾음" );

    private final HttpStatus status;
    private final String message;
}
