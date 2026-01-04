package com.example.jeogieottae.domain.usercoupon.repository;

import com.example.jeogieottae.domain.usercoupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    long countByUserIdAndCouponId(Long userId, Long id);

    long countByCouponId(Long couponId);
}
