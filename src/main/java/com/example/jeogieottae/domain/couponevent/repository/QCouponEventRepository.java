package com.example.jeogieottae.domain.couponevent.repository;

import com.example.jeogieottae.domain.couponevent.entity.CouponEvent;

import java.util.Optional;

public interface QCouponEventRepository {
    Optional<CouponEvent> findByIdForUpdate(Long id);
}
