package com.example.jeogieottae.domain.coupon.repository;

import com.example.jeogieottae.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, QCouponRepository {
}
