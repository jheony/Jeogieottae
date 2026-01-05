package com.example.jeogieottae.domain.accommodation.repository;

import com.example.jeogieottae.domain.accommodation.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, QAccommodationRepository {
}
