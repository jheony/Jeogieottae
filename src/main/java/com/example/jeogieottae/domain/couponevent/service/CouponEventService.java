package com.example.jeogieottae.domain.couponevent.service;

import com.example.jeogieottae.common.annotation.RedisLock;
import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.domain.coupon.entity.Coupon;
import com.example.jeogieottae.domain.couponevent.entity.CouponEvent;
import com.example.jeogieottae.domain.couponevent.repository.CouponEventRepository;
import com.example.jeogieottae.domain.user.entity.User;
import com.example.jeogieottae.domain.user.repository.UserRepository;
import com.example.jeogieottae.domain.usercoupon.dto.response.UserCouponResponse;
import com.example.jeogieottae.domain.usercoupon.entity.UserCoupon;
import com.example.jeogieottae.domain.usercoupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponEventService {

    private final CouponEventRepository couponEventRepository;
    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    @RedisLock(key="lock:couponEvent")
    public UserCouponResponse issueCouponEvent(Long userId, Long couponEventId) {

        CouponEvent event = couponEventRepository.findById(couponEventId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        event.validateIssuable();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Coupon coupon = event.getCoupon();

        if (userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())) {
            throw new CustomException(ErrorCode.COUPON_ALREADY_ISSUED);
        }

        event.decreaseQuantity();

        UserCoupon userCoupon = UserCoupon.create(user, coupon);
        userCouponRepository.save(userCoupon);

        return UserCouponResponse.from(coupon);
    }
}
