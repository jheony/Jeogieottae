package com.example.jeogieottae.domain.reservation.service;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.common.response.CustomPageResponse;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationRequest;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationResponse;
import com.example.jeogieottae.domain.reservation.dto.ReservationDto;
import com.example.jeogieottae.domain.reservation.dto.ReservationResponse;
import com.example.jeogieottae.domain.reservation.entity.Reservation;
import com.example.jeogieottae.domain.reservation.repository.ReservationRepository;
import com.example.jeogieottae.domain.room.entity.Room;
import com.example.jeogieottae.domain.room.repository.RoomRepository;
import com.example.jeogieottae.domain.user.entity.User;
import com.example.jeogieottae.domain.user.repository.UserRepository;
import com.example.jeogieottae.domain.usercoupon.entity.UserCoupon;
import com.example.jeogieottae.domain.usercoupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateReservationResponse createReservation(
            Long roomId,
            Long userCouponId,
            Long userId,
            CreateReservationRequest request
    ) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId).orElseThrow(
                () -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        if (userCoupon.isUsed()) {
            throw new CustomException(ErrorCode.COUPON_ALREADY_ISSUED);
        }

        Boolean ableToReservationFlag = reservationRepository.ableToReservation(
                userId,
                roomId,
                request.getCheckIn(),
                request.getCheckOut()
        );

        if (!ableToReservationFlag) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_AVAILABLE);
        }

        Room room = roomRepository.getReferenceById(roomId);
        User user = userRepository.getReferenceById(userId);

        String couponName = userCoupon.getCoupon().getName();
        Long originalPrice = room.getPrice();
        Long discountPrice = originalPrice * (100 - userCoupon.getCoupon().getDiscountValue()) / 100;

        Reservation reservation = reservationRepository.save(Reservation.create(user, room, couponName, originalPrice, discountPrice, request));

        userCoupon.setUsed(true);

        ReservationDto dto = ReservationDto.from(reservation);
        String accommodationName = room.getAccommodation().getName();

        return CreateReservationResponse.from(dto, accommodationName);
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<ReservationResponse> getAllMyReservation(Long userId, Pageable pageable) {

        Page<ReservationResponse> response = reservationRepository.findAllById(userId, pageable);
        return CustomPageResponse.from(response);
    }

    @Transactional
    public ReservationResponse deleteReservation(Long userId, Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND)
        );

        if (!reservation.getUser().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        reservation.setIsDeleted(true);

        return ReservationResponse.from(ReservationDto.from(reservation));
    }
}
