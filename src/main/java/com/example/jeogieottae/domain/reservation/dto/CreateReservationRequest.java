package com.example.jeogieottae.domain.reservation.dto;

import com.example.jeogieottae.domain.reservation.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateReservationRequest {

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Long guest;
    private ReservationStatus status;
}
