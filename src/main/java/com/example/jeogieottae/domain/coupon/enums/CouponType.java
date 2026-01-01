package com.example.jeogieottae.domain.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponType {
    FIXED("FIXED"),
    RATE("RATE")
    ;

    private String type;

    CouponType(String type) {
        this.type = type;
    }
}
