package com.example.jeogieottae.domain.reservation.repository;

import com.example.jeogieottae.domain.reservation.dto.ReservationInfoDto;
import com.example.jeogieottae.domain.reservation.dto.ReservationResponse;
import com.example.jeogieottae.domain.reservation.entity.Reservation;
import com.example.jeogieottae.domain.usercoupon.entity.UserCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface QReservationRepository {

    ReservationInfoDto findRoomAndUserNameWithOverlap(
            Long userId,
            Long roomId,
            LocalDateTime checkIn,
            LocalDateTime checkOut
    );

    UserCoupon findVaildUserCoupon(Long userId, Long userCouponId);

    Page<ReservationResponse> findAllById(Long userId, Pageable pageable);

    Reservation findByIdWithUserAndAccommodation(Long reservationId);
}
