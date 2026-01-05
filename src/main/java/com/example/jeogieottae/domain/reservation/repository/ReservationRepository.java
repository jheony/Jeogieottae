package com.example.jeogieottae.domain.reservation.repository;

import com.example.jeogieottae.domain.reservation.dto.ReservationResponse;
import com.example.jeogieottae.domain.reservation.entity.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT r
            FROM Reservation r
            WHERE r.room.id = :roomId
                AND r.isDeleted = FALSE
                AND r.checkIn < :checkOut
                AND r.checkOut > :checkIn
            """)
    List<Reservation> findAllOverlapReservation(
            Long userId,
            Long roomId,
            LocalDateTime checkIn,
            LocalDateTime checkOut
    );

    @Query("""
                    SELECT new com.example.jeogieottae.domain.reservation.dto.ReservationResponse(
                        u.username, r.discountedPrice, r.checkIn, r.checkOut
                        )
                    FROM Reservation r
                    JOIN r.user u
                    WHERE u.id = :userId
            """)
    Page<ReservationResponse> findAllById(Long userId, Pageable pageable);
}
