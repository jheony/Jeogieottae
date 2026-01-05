package com.example.jeogieottae.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //------409-----------------------
    USER_ALREADY_EXISTS(409, "이미 존재하는 사용자 이메일입니다."),
    COUPON_ALREADY_USED(409, "이미 사용한 쿠폰입니다."),
    RESERVATION_NOT_AVAILABLE(409, "예약일자가 기존 예약과 중복됩니다."),
    COUPON_EVENT_CONFLICT(409,"현재 쿠폰 발급 처리중입니다. 잠시 후 다시 시도해 주세요."),

    //------404-----------------------
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    COUPON_NOT_FOUND(404, "쿠폰을 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND(404, "예약을 찾을 수 없습니다."),
    ACCOMMODATION_NOT_FOUND(404, "숙소를 찾을 수 없습니다."),

    //------403-----------------------
    USER_NOT_MATCH(403, "접근 권한이 없습니다"),
    PASSWORD_NOT_MATCH(403, "비밀번호가 일치하지 않습니다."),
    FORBIDDEN(403, "접근 권한이 없습니다"),

    //------401-----------------------
    LOGIN_REQUIRED(401, "로그인한 유저만 사용할 수 있는 기능입니다"),
    LOGIN_UNAUTHORIZED(401, "아이디 또는 비밀번호가 올바르지 않습니다."),

    //------400-----------------------
    INVALID_EMAIL_FORMAT(400, "이메일 형식이 올바르지 않습니다."),
    INVALID_PASSWORD_FORMAT(400, "비밀번호 형식이 올바르지 않습니다."),
    INVALID_PASSWORD(400, "비밀번호가 유효하지 않습니다."),
    VALIDATION_ERROR(400, "입력값이 유효하지 않습니다."),
    COUPON_ALREADY_ISSUED(400, "이미 발급받은 쿠폰입니다."),
    COUPON_EVENT_NOT_AVAILABLE(400, "이용할 수 없는 쿠폰입니다."),
    ALL_COUPON_ALREADY_ISSUED(400, "쿠폰이 모두 소진되었습니다.")
    ;
    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}

