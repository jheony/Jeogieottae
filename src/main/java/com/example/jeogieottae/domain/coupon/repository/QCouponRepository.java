package com.example.jeogieottae.domain.coupon.repository;

import com.example.jeogieottae.domain.accommodation.enums.AccommodationType;
import com.example.jeogieottae.domain.coupon.entity.Coupon;
import com.example.jeogieottae.domain.coupon.enums.CouponType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface QCouponRepository {
    Slice<Coupon> findCouponList(AccommodationType accommodation, CouponType discount, Long minPrice, Pageable pageable);
}
