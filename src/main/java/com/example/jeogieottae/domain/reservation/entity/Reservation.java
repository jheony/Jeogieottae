package com.example.jeogieottae.domain.reservation.entity;

import com.example.jeogieottae.domain.reservation.dto.CreateReservationRequest;
import com.example.jeogieottae.domain.reservation.enums.ReservationPayment;
import com.example.jeogieottae.domain.reservation.enums.ReservationStatus;
import com.example.jeogieottae.domain.room.entity.Room;
import com.example.jeogieottae.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations", indexes = {
        @Index(name = "idx_room_deleted_checkin_checkout",
                columnList = "room_id, is_deleted, check_in, check_out")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDateTime checkOut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "coupon_name")
    private String couponName;

    @Column(name = "guest_count", nullable = false)
    private Long guestCount;

    @Column(name = "original_price")
    private Long originalPrice;

    @Column(name = "discounted_price", nullable = false)
    private Long discountedPrice;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Setter
    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationPayment payment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "payment_deadline")
    private LocalDateTime paymentDeadline;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @PrePersist
    public void prePersist() {

        if (this.status == null) this.status = ReservationStatus.RESERVED;
        if (this.payment == null) this.payment = ReservationPayment.PENDING;
        if (this.isDeleted == null) this.isDeleted = false;
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.paymentDeadline == null) this.paymentDeadline = this.createdAt.plusMinutes(10);
    }

    public static Reservation create(User user,
                                     Room room,
                                     String couponName,
                                     Long originalPrice,
                                     Long discountedPrice,
                                     CreateReservationRequest request) {

        Reservation reservation = new Reservation();

        reservation.checkIn = request.getCheckIn();
        reservation.checkOut = request.getCheckOut();
        reservation.user = user;
        reservation.room = room;
        reservation.couponName = couponName;
        reservation.guestCount = request.getGuest();
        reservation.originalPrice = originalPrice;
        reservation.discountedPrice = discountedPrice;

        return reservation;
    }
}
