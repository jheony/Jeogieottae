package com.example.jeogieottae.domain.reservation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationPayment {

    PENDING("PENDING"),
    SUCCESS("SUCCESS"),
    REFUNDED("REFUNDED")
    ;

    private String status;

    ReservationPayment(String status) {
        this.status = status;
    }
}
