package com.example.jeogieottae.domain.couponevent.entity;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.domain.coupon.entity.Coupon;
import com.example.jeogieottae.domain.coupon.enums.CouponEventStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CouponEventStatus status;

    @Column(name = "total_quantity", nullable = false)
    private long totalQuantity;

    @Column(name = "available_quantity", nullable = false)
    private long availableQuantity = 0L;

    public static CouponEvent create(
            Coupon coupon,
            LocalDateTime startAt,
            LocalDateTime endAt,
            long totalQuantity
    ) {
        CouponEvent event = new CouponEvent();
        event.coupon = coupon;
        event.startAt = startAt;
        event.endAt = endAt;
        event.totalQuantity = totalQuantity;
        event.availableQuantity = totalQuantity;
        event.status = CouponEventStatus.UPCOMING;
        return event;
    }

    public void validateIssuable() {
        if (status != CouponEventStatus.ONGOING) {
            throw new CustomException(ErrorCode.COUPON_EVENT_NOT_AVAILABLE);
        }

        if (availableQuantity <= 0) {
            throw new CustomException(ErrorCode.ALL_COUPON_ALREADY_ISSUED);
        }
    }

    public void decreaseQuantity() {
        this.availableQuantity --;
    }
}
