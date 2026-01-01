package com.example.jeogieottae.domain.usercoupon.dto.response;

import com.example.jeogieottae.domain.coupon.entity.Coupon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserCouponResponse {

    private final String name;
    private final Long discountValue;
    private final LocalDateTime expiresAt;
    private final Long conditionMinPrice;
    private final String conditionAccommodationType;

    public static UserCouponResponse from(Coupon coupon) {

        return new UserCouponResponse(
                coupon.getName(),
                coupon.getDiscountValue(),
                coupon.getExpiresAt(),
                coupon.getConditionMinPrice(),
                coupon.getConditionAccommodationType()
        );
    }
}
