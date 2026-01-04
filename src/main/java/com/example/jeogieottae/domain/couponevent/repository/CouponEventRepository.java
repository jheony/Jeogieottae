package com.example.jeogieottae.domain.couponevent.repository;

import com.example.jeogieottae.domain.couponevent.entity.CouponEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponEventRepository extends JpaRepository <CouponEvent, Long> , QCouponEventRepository {
}
