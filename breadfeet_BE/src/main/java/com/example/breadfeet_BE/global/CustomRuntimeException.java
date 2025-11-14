package com.example.breadfeet_BE.global;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Getter
@Slf4j
public class CustomRuntimeException extends RuntimeException {

    private final HttpStatus status;
    private final String message;
    private final String code;

    public CustomRuntimeException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.code = errorCode.name();

        log.error("TugoRuntimeException 발생: status={}, message={}", status, message, this);
    }
}