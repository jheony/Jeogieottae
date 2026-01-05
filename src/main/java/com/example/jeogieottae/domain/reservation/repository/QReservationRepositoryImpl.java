package com.example.jeogieottae.domain.reservation.repository;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.domain.reservation.dto.ReservationInfoDto;
import com.example.jeogieottae.domain.reservation.dto.ReservationResponse;
import com.example.jeogieottae.domain.reservation.entity.Reservation;
import com.example.jeogieottae.domain.specialprice.entity.QSpecialPrice;
import com.example.jeogieottae.domain.usercoupon.entity.UserCoupon;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.jeogieottae.domain.accommodation.entity.QAccommodation.accommodation;
import static com.example.jeogieottae.domain.reservation.entity.QReservation.reservation;
import static com.example.jeogieottae.domain.room.entity.QRoom.room;
import static com.example.jeogieottae.domain.user.entity.QUser.user;
import static com.example.jeogieottae.domain.usercoupon.entity.QUserCoupon.userCoupon;

@RequiredArgsConstructor
public class QReservationRepositoryImpl implements QReservationRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public ReservationInfoDto findRoomAndUserNameWithOverlap(Long userId, Long roomId, LocalDateTime checkIn, LocalDateTime checkOut) {

        return queryFactory
                .select(Projections.constructor(ReservationInfoDto.class,
                        reservation.id,
                        room,
                        accommodation.name,
                        user.username,
                        room.specialPrice.discount))
                .from(room)
                .join(room.accommodation, accommodation)
                .join(user).on(user.id.eq(userId))
                .leftJoin(room.specialPrice, QSpecialPrice.specialPrice)
                .leftJoin(reservation).on(
                        reservation.room.id.eq(roomId),
                        reservation.isDeleted.isFalse(),
                        reservation.checkIn.lt(checkOut),
                        reservation.checkOut.gt(checkIn)
                )
                .where(room.id.eq(roomId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }

    @Override
    public UserCoupon findVaildUserCoupon(Long userId, Long userCouponId) {

        if (userCouponId == null) {
            return null;
        }
        UserCoupon coupon = queryFactory
                .selectFrom(userCoupon)
                .join(userCoupon.user, user).fetchJoin()
                .join(userCoupon.coupon).fetchJoin()
                .where(
                        userCoupon.id.eq(userCouponId),
                        userCoupon.user.id.eq(userId),
                        userCoupon.isUsed.isFalse()
                )
                .fetchOne();

        if (coupon == null) {
            throw new CustomException(ErrorCode.COUPON_NOT_FOUND);
        }

        return coupon;
    }

    @Override
    public Page<ReservationResponse> findAllById(Long userId, Pageable pageable) {

        List<ReservationResponse> result = queryFactory
                .select(Projections.constructor(ReservationResponse.class,
                        user.username,
                        accommodation.name,
                        reservation.checkIn,
                        reservation.checkOut
                        ))
                .from(reservation)
                .join(reservation.user, user)
                .join(reservation.room, room)
                .join(room.accommodation, accommodation)
                .where(
                        user.id.eq(userId),
                        reservation.isDeleted.isFalse()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(reservation.createdAt.desc())
                .fetch();

        long total = result.size();

        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Reservation findByIdWithUserAndAccommodation(Long reservationId) {

        return queryFactory
                .selectFrom(reservation)
                .join(reservation.user, user).fetchJoin()
                .join(reservation.room, room).fetchJoin()
                .join(room.accommodation, accommodation).fetchJoin()
                .where(reservation.id.eq(reservationId))
                .fetchOne();

    }
}
