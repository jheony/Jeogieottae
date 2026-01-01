package com.example.jeogieottae.domain.reservation.dto;

import com.example.jeogieottae.domain.reservation.entity.Reservation;
import com.example.jeogieottae.domain.reservation.enums.ReservationStatus;
import com.example.jeogieottae.domain.room.entity.Room;
import com.example.jeogieottae.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationDto {

    private Long id;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private User user;
    private Room room;
    private Long userCouponId;
    private Long guestCount;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private Boolean isDeleted;

    public static ReservationDto from(Reservation reservation) {

        return new ReservationDto(
                reservation.getId(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getUser(),
                reservation.getRoom(),
                reservation.getUserCouponId(),
                reservation.getGuestCount(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getIsDeleted()
        );

    }
}
