package com.example.jeogieottae.domain.accommodation.service;

import com.example.jeogieottae.common.response.CustomPageResponse;
import com.example.jeogieottae.domain.accommodation.dto.response.AccommodationResponse;
import com.example.jeogieottae.domain.accommodation.entity.Accommodation;
import com.example.jeogieottae.domain.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    @Transactional(readOnly = true)
    public CustomPageResponse<AccommodationResponse> getAccommodationList(Pageable pageable) {

        Page<Accommodation> accommodationPage = accommodationRepository.findAll(pageable);
        Page<AccommodationResponse> accommodationResponsePage = accommodationPage
                .map(AccommodationResponse::from);
        return CustomPageResponse.from(accommodationResponsePage);
    }

}
