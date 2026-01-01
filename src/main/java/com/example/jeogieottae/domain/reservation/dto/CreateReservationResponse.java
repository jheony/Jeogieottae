package com.example.jeogieottae.domain.reservation.dto;

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

    public static CreateReservationResponse from(ReservationDto dto, String accommodationName, Long discountPrice) {

        return new CreateReservationResponse(
                dto.getUser().getUsername(),
                accommodationName,
                dto.getCheckIn(),
                dto.getCheckOut(),
                dto.getGuestCount(),
                discountPrice
        );
    }
}
