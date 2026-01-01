package com.example.jeogieottae.domain.usercoupon.controller;

import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.usercoupon.dto.response.UserCouponResponse;
import com.example.jeogieottae.domain.usercoupon.service.UserCouponService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;

    @PostMapping("/coupons/{couponId}")
    public ResponseEntity<GlobalResponse<UserCouponResponse>> issueCoupon(HttpServletRequest servletRequest, @PathVariable Long couponId) {

        Long userId = (Long) servletRequest.getAttribute("userId");

        UserCouponResponse response = userCouponService.issueCoupon(userId, couponId);

        return ResponseEntity.ok(GlobalResponse.success(true, "쿠폰 발급 성공", response));
    }
}
