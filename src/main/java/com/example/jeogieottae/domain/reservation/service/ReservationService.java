package com.example.jeogieottae.domain.reservation.service;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.common.response.CustomPageResponse;
import com.example.jeogieottae.domain.coupon.entity.Coupon;
import com.example.jeogieottae.domain.coupon.enums.CouponType;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationRequest;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationResponse;
import com.example.jeogieottae.domain.reservation.dto.ReservationInfoDto;
import com.example.jeogieottae.domain.reservation.dto.ReservationResponse;
import com.example.jeogieottae.domain.reservation.entity.Reservation;
import com.example.jeogieottae.domain.reservation.repository.ReservationRepository;
import com.example.jeogieottae.domain.room.entity.Room;
import com.example.jeogieottae.domain.user.entity.User;
import com.example.jeogieottae.domain.user.repository.UserRepository;
import com.example.jeogieottae.domain.usercoupon.entity.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateReservationResponse createReservation(
            Long userId,
            CreateReservationRequest request
    ) {

        ReservationInfoDto userRoom = reservationRepository.findRoomAndUserNameWithOverlap(
                userId,
                request.getRoomId(),
                request.getCheckIn(),
                request.getCheckOut()
        );

        if (userRoom.getOverlapReservationId() != null) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_AVAILABLE);
        }

        UserCoupon userCoupon = reservationRepository.findVaildUserCoupon(userId, request.getUserCouponId());

        Room room = userRoom.getRoom();
        User user = userRepository.getReferenceById(userId);

        Long originalPrice = room.getPrice();
        Long specialPrice = room.getSpecialPrice() == null
                ? originalPrice
                : (originalPrice * (100 - userRoom.getSpecialPriceDiscount()) / 100);

        Coupon coupon = userCoupon != null ? userCoupon.getCoupon() : null;
        String couponName = coupon != null ? coupon.getName() : null;

        Long discountPrice = getDiscountPrice(specialPrice, userCoupon);

        Reservation reservation = reservationRepository.save(
                Reservation.create(user, room, couponName, originalPrice, discountPrice, request));

        return CreateReservationResponse.from(reservation, userRoom);
    }

    private Long getDiscountPrice(Long specialPrice, UserCoupon userCoupon) {

        Long discountPrice = specialPrice;

        Coupon coupon = userCoupon != null ? userCoupon.getCoupon() : null;

        if (coupon != null && specialPrice >= coupon.getMinPrice()) {
            discountPrice = coupon.getDiscountType().equals(CouponType.RATE)
                    ? (specialPrice * (100 - coupon.getDiscountValue()) / 100)
                    : specialPrice - coupon.getDiscountValue();

            userCoupon.setUsed(true);
        }
        return discountPrice;
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<ReservationResponse> getMyReservationList(Long userId, Pageable pageable) {

        Page<ReservationResponse> response = reservationRepository.findAllById(userId, pageable);
        return CustomPageResponse.from(response);
    }

    @Transactional(readOnly = true)
    public ReservationResponse getMyReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findByIdWithUserAndAccommodation(reservationId);

        if (reservation.getIsDeleted()) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        return ReservationResponse.from(reservation);
    }

    @Transactional
    public ReservationResponse deleteReservation(Long userId, Long reservationId) {

        Reservation reservation = reservationRepository.findByIdWithUserAndAccommodation(reservationId);

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
