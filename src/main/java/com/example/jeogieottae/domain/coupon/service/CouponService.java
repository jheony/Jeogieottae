package com.example.jeogieottae.domain.coupon.service;

import com.example.jeogieottae.common.response.CustomPageResponse;
import com.example.jeogieottae.domain.accommodation.enums.AccommodationType;
import com.example.jeogieottae.domain.coupon.dto.response.CouponResponse;
import com.example.jeogieottae.domain.coupon.entity.Coupon;
import com.example.jeogieottae.domain.coupon.enums.CouponType;
import com.example.jeogieottae.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public Slice<CouponResponse> getCouponList(Pageable pageable,
                                                            AccommodationType accommodation,
                                                            CouponType discount,
                                                            Long minPrice
    ) {
        Slice<Coupon> couponPageList = couponRepository.findCouponList(accommodation, discount, minPrice, pageable);

        return couponPageList.map(CouponResponse::from);
    }
}
