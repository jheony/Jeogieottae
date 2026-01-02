package com.example.jeogieottae.domain.reservation.dto;

import com.example.jeogieottae.domain.reservation.entity.Reservation;
import com.example.jeogieottae.domain.reservation.enums.ReservationStatus;
import com.example.jeogieottae.domain.room.dto.RoomDto;
import com.example.jeogieottae.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationDto {

    private Long id;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private UserDto user;
    private RoomDto room;
    private String couponName;
    private Long guestCount;
    private Long originalPrice;
    private Long discountedPrice;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private Boolean isDeleted;

    public static ReservationDto from(Reservation reservation) {

        return new ReservationDto(
                reservation.getId(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                UserDto.from(reservation.getUser()),
                RoomDto.from(reservation.getRoom()),
                reservation.getCouponName(),
                reservation.getGuestCount(),
                reservation.getOriginalPrice(),
                reservation.getDiscountedPrice(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getIsDeleted()
        );

    }
}
