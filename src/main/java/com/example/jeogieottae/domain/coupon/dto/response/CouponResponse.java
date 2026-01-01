package com.example.jeogieottae.domain.coupon.dto.response;

import com.example.jeogieottae.domain.coupon.entity.Coupon;
import com.example.jeogieottae.domain.coupon.enums.AccommodationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CouponResponse {

    private final Long id;
    private final String name;
    private final Long discountValue;
    private final LocalDateTime expiresAt;
    private final Long conditionMinPrice;
    private final AccommodationType conditionAccomodationType;

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountValue(),
                coupon.getExpiresAt(),
                coupon.getConditionMinPrice(),
                coupon.getConditionAccommodationType()
        );
    }

}

