package com.example.jeogieottae.domain.reservation.service;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.common.response.CustomPageResponse;
import com.example.jeogieottae.domain.coupon.entity.Coupon;
import com.example.jeogieottae.domain.coupon.enums.CouponType;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationRequest;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationResponse;
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
            Long userId,
            CreateReservationRequest request
    ) {

        Boolean ableToReservationFlag = reservationRepository.ableToReservation(
                userId,
                request.getRoomId(),
                request.getCheckIn(),
                request.getCheckOut()
        );

        if (!ableToReservationFlag) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_AVAILABLE);
        }

        UserCoupon userCoupon = null;

        if (request.getUserCouponId() != null) {
            userCoupon = userCouponRepository.findById(request.getUserCouponId()).orElseThrow(
                    () -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

            if (userCoupon.isUsed()) {
                throw new CustomException(ErrorCode.COUPON_ALREADY_ISSUED);
            }
        }

        Room room = roomRepository.getReferenceById(request.getRoomId());
        User user = userRepository.getReferenceById(userId);

        Long originalPrice = room.getPrice();
        Long specialPrice = room.getSpecialPrice() == null
                            ? originalPrice
                            : (originalPrice * (100 - room.getSpecialPrice().getDiscount()) / 100);

        if (userCoupon == null) {
            Reservation reservation = reservationRepository.save(
                    Reservation.create(user, room, null, originalPrice, specialPrice, request));

            return CreateReservationResponse.from(reservation);
        }

        Long discountPrice = originalPrice * (100 - userCoupon.getCoupon().getDiscountValue()) / 100;

        Coupon coupon = userCoupon.getCoupon();

        if (specialPrice >= coupon.getMinPrice()) {
            discountPrice = coupon.getDiscountType().equals(CouponType.RATE)
                            ? (specialPrice * (100 - coupon.getDiscountValue()) / 100)
                            : specialPrice - coupon.getDiscountValue();
        }
        Reservation reservation = reservationRepository.save(
                Reservation.create(user, room, coupon.getName(), originalPrice, discountPrice, request));

        userCoupon.setUsed(true);

        return CreateReservationResponse.from(reservation);
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

        if (reservation.getIsDeleted()) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_FOUND);
        }

        if (!reservation.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        reservation.setIsDeleted(true);

        return ReservationResponse.from(reservation);
    }
}
