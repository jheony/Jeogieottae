package com.example.jeogieottae.domain.coupon.entity;

import com.example.jeogieottae.domain.coupon.enums.AccommodationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String type;

    @Column(name = "discount_value", nullable = false)
    private Long discountValue;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "condition_min_price")
    private Long conditionMinPrice;


    @Column(name = "condition_accommodation_type", length = 50)
    @Enumerated(EnumType.STRING)
    private AccommodationType conditionAccommodationType;
}
