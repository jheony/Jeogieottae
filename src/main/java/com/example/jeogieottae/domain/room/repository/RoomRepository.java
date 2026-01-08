package com.example.jeogieottae.domain.room.repository;

import com.example.jeogieottae.domain.room.entity.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @EntityGraph(attributePaths = {"specialPrice"})
    List<Room> findAllByAccommodationIdOrderByPriceAsc(Long accommodationId);

    List<Room> findByAccommodationIdIn(List<Long> accommodationIds);

    List<Room> findByAccommodationId(Long accommodationId);
}
