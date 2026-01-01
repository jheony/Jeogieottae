package com.example.jeogieottae.domain.coupon.controller;

import com.example.jeogieottae.common.response.CustomPageResponse;
import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.coupon.dto.response.CouponResponse;
import com.example.jeogieottae.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<GlobalResponse<CustomPageResponse<CouponResponse>>> getAllCoupons(

            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "expiresAt",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {
        CustomPageResponse<CouponResponse> result = couponService.getAllCoupons(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "쿠폰 목록 조회 성공", result));
    }
}
