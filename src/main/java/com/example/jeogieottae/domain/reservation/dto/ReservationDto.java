package com.example.jeogieottae.domain.reservation.dto;

import com.example.jeogieottae.domain.reservation.entity.Reservation;
import com.example.jeogieottae.domain.reservation.enums.ReservationStatus;
import com.example.jeogieottae.domain.room.entity.Room;
import com.example.jeogieottae.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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

    public ReservationDto(Reservation reservation) {

        this.id = reservation.getId();
        this.checkIn = reservation.getCheckIn();
        this.checkOut = reservation.getCheckOut();
        this.user = reservation.getUser();
        this.room = reservation.getRoom();
        this.userCouponId = reservation.getUserCouponId();
        this.guestCount = reservation.getGuestCount();
        this.status = reservation.getStatus();
        this.createdAt = reservation.getCreatedAt();
        this.isDeleted = reservation.getIsDeleted();
    }
}
