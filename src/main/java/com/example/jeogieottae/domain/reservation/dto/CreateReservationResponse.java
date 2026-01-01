package com.example.jeogieottae.domain.reservation.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateReservationResponse {
    private final String userName;
    private final String accommodationName;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer guest;
    private final Integer discount;

    public CreateReservationResponse(String userName, String accommodationName, LocalDateTime startDate, LocalDateTime endDate, Integer guest, Integer discount) {
        this.userName = userName;
        this.accommodationName = accommodationName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guest = guest;
        this.discount = discount;
    }
}
