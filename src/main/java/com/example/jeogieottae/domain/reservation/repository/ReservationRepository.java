package com.example.jeogieottae.domain.reservation.repository;

import com.example.jeogieottae.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
            SELECT CASE
                    WHEN NOT EXISTS(
                                SELECT 1
                                FROM User u
                                WHERE u.id = :userId
                                )
                    THEN FALSE
                    WHEN EXISTS(
                        SELECT 1
                        FROM Reservation r
                        WHERE r.room.id = :roomId
                            AND r.isDeleted = FALSE
                            AND r.checkIn < :checkOut
                            AND r.checkOut > :checkIn
                        )
                    THEN FALSE
                    ELSE TRUE
                END
            """)
    Boolean ableToReservation(
            Long userId,
            Long roomId,
            LocalDateTime checkIn,
            LocalDateTime checkOut
    );
}
