package com.example.jeogieottae.domain.coupon.controller;

import com.example.jeogieottae.common.response.CustomPageResponse;
import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.accommodation.enums.AccommodationType;
import com.example.jeogieottae.domain.coupon.dto.response.CouponResponse;
import com.example.jeogieottae.domain.coupon.enums.CouponType;
import com.example.jeogieottae.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            Pageable pageable,
            @RequestParam(required = false) String accommodation,
            @RequestParam(required = false) String discount,
            @RequestParam(required = false) Long minPrice
    ) {
        AccommodationType accommodationType = AccommodationType.valueOf(accommodation);
        CouponType discountType = CouponType.valueOf(discount);

        CustomPageResponse<CouponResponse> result = couponService.getAllCoupons(pageable, accommodationType, discountType, minPrice);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "쿠폰 목록 조회 성공", result));
    }
}
