package com.example.jeogieottae.domain.reservation.dto;

import com.example.jeogieottae.domain.reservation.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class CreateReseravtionRequest {
    @Setter
    private Long roomId;
    @Setter
    private Long userId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Integer guestCount;
    private ReservationStatus status;
}
