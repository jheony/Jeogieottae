package com.example.jeogieottae.domain.reservation.dto;

import com.example.jeogieottae.domain.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReservationResponse {
    private final String name;
    private final String accommodationName;
    private final Long price;
    private final LocalDateTime checkIn;
    private final LocalDateTime checkOut;

    public static ReservationResponse from(Reservation reservation) {

        return new ReservationResponse(
                reservation.getUser().getUsername(),
                reservation.getRoom().getAccommodation().getName(),
                reservation.getDiscountedPrice(),
                reservation.getCheckIn(),
                reservation.getCheckOut()
        );
    }
}
