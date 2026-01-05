package com.example.jeogieottae.domain.coupon.repository;

import com.example.jeogieottae.domain.accommodation.enums.AccommodationType;
import com.example.jeogieottae.domain.coupon.entity.Coupon;
import com.example.jeogieottae.domain.coupon.enums.CouponType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM Coupon c WHERE (:accommodation IS NULL OR c.accommodationType = :accommodation) " +
            "AND (:discount IS NULL OR c.discountType = :discount)" +
            "AND (:minPrice IS NULL OR c.minPrice >= :minPrice)")
    Page<Coupon> findCouponList(Pageable pageable, @Param("accommodation") AccommodationType accommodation, @Param("discount") CouponType discount, @Param("minPrice") Long minPrice);
}
