package com.example.jeogieottae.domain.reservation.dto;

import com.example.jeogieottae.domain.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CreateReservationResponse {

    private final String userName;
    private final String accommodationName;
    private final LocalDateTime checkIn;
    private final LocalDateTime checkOut;
    private final Long guest;
    private final Long discountPrice;

    public static CreateReservationResponse from(Reservation reservation) {

        return new CreateReservationResponse(
                reservation.getUser().getUsername(),
                reservation.getRoom().getAccommodation().getName(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getGuestCount(),
                reservation.getDiscountedPrice()
        );
    }
}
