package com.example.jeogieottae.domain.reservation.dto;

import com.example.jeogieottae.domain.room.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationInfoDto {
    private Long overlapReservationId;
    private Room room;
    private String accommodationName;
    private String username;
    private Long specialPriceDiscount;
}
