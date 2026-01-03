package com.example.jeogieottae.domain.couponevent.service;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.domain.coupon.enums.CouponEventStatus;
import com.example.jeogieottae.domain.couponevent.entity.CouponEvent;
import com.example.jeogieottae.domain.couponevent.repository.CouponEventRepository;
import com.example.jeogieottae.domain.usercoupon.dto.response.UserCouponResponse;
import com.example.jeogieottae.domain.usercoupon.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponEventService {

    private final CouponEventRepository couponEventRepository;
    private final UserCouponService userCouponService;

    @Transactional
    public UserCouponResponse issueCouponEvent(Long userId, Long couponEventId) {

        CouponEvent event = couponEventRepository.findById(couponEventId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        if (event.getStatus() != CouponEventStatus.ONGOING) {
            throw new CustomException(ErrorCode.COUPON_EVENT_NOT_AVAILABLE);
        }

        event.issue();

        return userCouponService.issueCoupon(
                userId,
                event.getCoupon().getId()
        );

    }
}
