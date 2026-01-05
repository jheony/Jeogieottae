package com.example.jeogieottae.domain.accommodation.repository;

import com.example.jeogieottae.domain.accommodation.dto.condition.SearchAccommodationCond;
import com.example.jeogieottae.domain.accommodation.dto.response.AccommodationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QAccommodationRepository {

    Page<AccommodationResponse> searchAccommodations(SearchAccommodationCond cond, Pageable pageable);
}