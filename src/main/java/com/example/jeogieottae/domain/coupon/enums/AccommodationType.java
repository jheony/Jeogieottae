package com.example.jeogieottae.domain.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccommodationType {

    HOTEL("HOTEL"),
    POOL_VILLA("POOL_VILLA"),
    RESORT("RESORT"),
    MOTEL("MOTEL")
    ;
    private String type;

    AccommodationType(String type) {
        this.type = type;
    }

}
