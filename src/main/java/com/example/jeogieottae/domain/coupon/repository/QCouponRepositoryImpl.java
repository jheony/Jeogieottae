package com.example.jeogieottae.domain.coupon.repository;

import com.example.jeogieottae.domain.accommodation.enums.AccommodationType;
import com.example.jeogieottae.domain.coupon.entity.Coupon;
import com.example.jeogieottae.domain.coupon.entity.QCoupon;
import com.example.jeogieottae.domain.coupon.enums.CouponType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

@RequiredArgsConstructor
public class QCouponRepositoryImpl implements QCouponRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Coupon> findCouponList(AccommodationType accommodation, CouponType discount, Long minPrice, Pageable pageable) {

        QCoupon coupon = QCoupon.coupon;

        BooleanExpression condition_accommodation = coupon.accommodationType.eq(accommodation);
        BooleanExpression condition_discount = coupon.discountType.eq(discount);
        BooleanExpression condition_minPrice = coupon.minPrice.goe(minPrice);

        List<Coupon> results = queryFactory.selectFrom(coupon)
                .where(
                        condition_accommodation,
                        condition_discount,
                        condition_minPrice)
                .orderBy(coupon.expiresAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        boolean hasNext = results.size() != pageable.getPageSize() &&
                queryFactory.selectFrom(coupon)
                        .where(
                                condition_accommodation,
                                condition_discount,
                                condition_minPrice)
                        .offset(pageable.getOffset() + pageable.getPageSize())
                        .limit(1)
                        .fetch().isEmpty();

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
