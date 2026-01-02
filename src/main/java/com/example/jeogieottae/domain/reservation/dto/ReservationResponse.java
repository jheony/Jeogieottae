package com.example.jeogieottae.domain.reservation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReservationResponse {
    private final String name;
    private final Long price;
    private final LocalDateTime checkIn;
    private final LocalDateTime checkOut;

    public static ReservationResponse from(ReservationDto dto) {

        return new ReservationResponse(
                dto.getUser().getUsername(),
                dto.getDiscountedPrice(),
                dto.getCheckIn(),
                dto.getCheckOut()
        );
    }
}
