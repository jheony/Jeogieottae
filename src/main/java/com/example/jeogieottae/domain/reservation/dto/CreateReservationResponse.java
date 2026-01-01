package com.example.jeogieottae.domain.reservation.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateReservationResponse {
    private final String userName;
    private final String accommodationName;
    private final LocalDateTime checkIn;
    private final LocalDateTime checkOut;
    private final Long guest;
    private final Long discountPrice;

    public CreateReservationResponse(ReservationDto dto, String accommodationName, Long discountPrice) {

        this.userName = dto.getUser().getUsername();
        this.accommodationName = accommodationName;
        this.checkIn = dto.getCheckIn();
        this.checkOut = dto.getCheckOut();
        this.guest = dto.getGuestCount();
        this.discountPrice = discountPrice;
    }
}
