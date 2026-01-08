package com.example.jeogieottae.domain.reservation.repository;

import com.example.jeogieottae.domain.reservation.entity.Reservation;
import com.example.jeogieottae.domain.reservation.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, QReservationRepository {

    List<Reservation> findByRoomIdInAndStatusNotInAndIsDeletedFalse(List<Long> roomIds, List<ReservationStatus> statusList);
}
