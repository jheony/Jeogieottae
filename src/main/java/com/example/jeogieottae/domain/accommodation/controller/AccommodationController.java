package com.example.jeogieottae.domain.accommodation.controller;

import com.example.jeogieottae.common.response.CustomPageResponse;
import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.accommodation.dto.condition.SearchAccommodationCond;
import com.example.jeogieottae.domain.accommodation.dto.response.AccommodationResponse;
import com.example.jeogieottae.domain.accommodation.dto.response.GetAccommodationCacheResponse;
import com.example.jeogieottae.domain.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping
    public ResponseEntity<GlobalResponse<CustomPageResponse<AccommodationResponse>>> searchAccommodations(
            @ModelAttribute SearchAccommodationCond cond,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        CustomPageResponse<AccommodationResponse> result = accommodationService.searchAccommodations(cond, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "숙소 목록 조회 성공", result));
    }

    @GetMapping("/{accommodationId}")
    public ResponseEntity<GlobalResponse<GetAccommodationCacheResponse>> getAccommodation(@PathVariable Long accommodationId) {

        GetAccommodationCacheResponse response = accommodationService.getAccommodation(accommodationId);

        return ResponseEntity.ok(GlobalResponse.success(true, "숙소 상세 조회 성공", response));
    }

}
