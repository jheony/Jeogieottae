package com.example.jeogieottae.domain.usercoupon.entity;

import com.example.jeogieottae.domain.coupon.entity.Coupon;
import com.example.jeogieottae.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "user_coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Setter
    @Column(name = "is_used")
    private boolean isUsed;

    public static UserCoupon create(User user, Coupon coupon) {

        UserCoupon userCoupon = new UserCoupon();

        userCoupon.user = user;
        userCoupon.coupon = coupon;
        userCoupon.isUsed = false;

        return userCoupon;
    }
}
