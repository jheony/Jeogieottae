package com.example.jeogieottae.domain.reservation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {

    RESERVED("RESERVED"),
    VISITED("VISITED"),
    CANCEL("CANCEL")
    ;

    private String status;

    ReservationStatus(String status) {
        this.status = status;
    }
}
