package com.example.jeogieottae.domain.accommodation.repository;

import com.example.jeogieottae.domain.accommodation.entity.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    Page<Accommodation> findAll(Pageable pageable);

    List<Accommodation> findByIdGreaterThanOrderByIdAsc(Long id, Pageable pageable);
}
