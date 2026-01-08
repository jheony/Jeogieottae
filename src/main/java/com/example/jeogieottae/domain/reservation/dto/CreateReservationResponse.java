package com.example.jeogieottae.domain.reservation.dto;

import com.example.jeogieottae.domain.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CreateReservationResponse {

    private final Long reservationId;
    private final String userName;
    private final String accommodationName;
    private final LocalDateTime checkIn;
    private final LocalDateTime checkOut;
    private final Long guest;
    private final Long discountPrice;

    public static CreateReservationResponse from(Reservation reservation, ReservationInfoDto dto) {

        return new CreateReservationResponse(
                reservation.getId(),
                dto.getUsername(),
                dto.getAccommodationName(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getGuestCount(),
                reservation.getDiscountedPrice()
        );
    }
}
