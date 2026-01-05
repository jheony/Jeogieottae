package com.example.jeogieottae.domain.couponevent.repository;

import com.example.jeogieottae.domain.couponevent.entity.CouponEvent;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.example.jeogieottae.domain.couponevent.entity.QCouponEvent.couponEvent;

@RequiredArgsConstructor
public class QCouponEventRepositoryImpl implements QCouponEventRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CouponEvent> findByIdForUpdate(Long id) {

        CouponEvent result = queryFactory
                .selectFrom(couponEvent)
                .where(couponEvent.id.eq(id))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
