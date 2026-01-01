package com.example.jeogieottae.domain.coupon.service;

import com.example.jeogieottae.common.response.CustomPageResponse;
import com.example.jeogieottae.domain.coupon.dto.response.CouponResponse;
import com.example.jeogieottae.domain.coupon.entity.Coupon;
import com.example.jeogieottae.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional(readOnly=true)
    public CustomPageResponse<CouponResponse> getAllCoupons(Pageable pageable) {

        Page<Coupon> couponPageList = couponRepository.findAll(pageable);
        Page<CouponResponse> couponResponsePage = couponPageList.map(CouponResponse::from);
        return CustomPageResponse.from(couponResponsePage);
    }

}
