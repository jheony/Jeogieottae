package com.example.jeogieottae.common.response;

import com.example.jeogieottae.common.exception.ErrorCode;

import java.time.LocalDateTime;

public class GlobalResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public GlobalResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    //성공시
    public static <T> GlobalResponse<T> success(boolean success, String message, T data) {
        return new GlobalResponse<>(success, message, data); //204 는 data null로 넣어주세요.
    }
    //예외처리시
    public static GlobalResponse<Void> exception(boolean success, ErrorCode errorCode) {
        return new GlobalResponse<>(success, errorCode.getMessage(), null);
    }
}
