package com.example.jeogieottae.domain.reservation.entity;

import com.example.jeogieottae.domain.reservation.dto.CreateReservationRequest;
import com.example.jeogieottae.domain.reservation.enums.ReservationStatus;
import com.example.jeogieottae.domain.room.entity.Room;
import com.example.jeogieottae.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
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

    @Column(name = "user_coupon_id")
    private Long userCouponId;

    @Column(name = "guest_count", nullable = false)
    private Long guestCount;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public static Reservation create(User user, Room room, Long userCouponId, CreateReservationRequest request) {

        Reservation reservation = new Reservation();

        reservation.checkIn = request.getCheckIn();
        reservation.checkOut = request.getCheckOut();
        reservation.user = user;
        reservation.room = room;
        reservation.userCouponId = userCouponId;
        reservation.guestCount = request.getGuest();
        reservation.status = request.getStatus();
        reservation.isDeleted = false;

        return reservation;
    }
}
