package com.example.jeogieottae.domain.couponevent.controller;

import com.example.jeogieottae.common.dto.AuthUser;
import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.couponevent.service.CouponEventService;
import com.example.jeogieottae.domain.usercoupon.dto.response.UserCouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon-events")
public class CouponEventController {

    private final CouponEventService couponEventService;

    @PostMapping("/{couponEventId}/issue")
    public ResponseEntity<GlobalResponse<UserCouponResponse>> issueCouponeEvent(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long couponEventId
    ) {
        UserCouponResponse response = couponEventService.issueCouponEvent(
                authUser.getUserId(),
                couponEventId
        );
        return ResponseEntity.ok(GlobalResponse.success(true, "이벤트 쿠폰 발급 성공", response));
    }
}
