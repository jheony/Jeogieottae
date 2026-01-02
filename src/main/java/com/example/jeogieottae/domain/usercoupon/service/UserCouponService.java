package com.example.jeogieottae.domain.usercoupon.service;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.domain.coupon.entity.Coupon;
import com.example.jeogieottae.domain.coupon.repository.CouponRepository;
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
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public UserCouponResponse issueCoupon(Long userId, Long couponId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        if(userCouponRepository.existsByUserIdAndCouponId(userId, couponId)){
            throw new CustomException(ErrorCode.COUPON_ALREADY_ISSUED);
        }

        UserCoupon userCoupon = UserCoupon.create(user, coupon);
        userCouponRepository.save(userCoupon);

        return UserCouponResponse.from(coupon);
    }
}
