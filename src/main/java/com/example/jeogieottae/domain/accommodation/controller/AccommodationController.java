package com.example.jeogieottae.domain.accommodation.controller;

import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.accommodation.dto.condition.SearchAccommodationCond;
import com.example.jeogieottae.domain.accommodation.dto.response.AccommodationResponse;
import com.example.jeogieottae.domain.accommodation.dto.response.GetAccommodationCacheResponse;
import com.example.jeogieottae.domain.accommodation.service.AccommodationSearchService;
import com.example.jeogieottae.domain.accommodation.service.AccommodationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;
    private final AccommodationSearchService accommodationSearchService;

    @GetMapping
    public ResponseEntity<GlobalResponse<Slice<AccommodationResponse>>> searchAccommodations(
            @ModelAttribute SearchAccommodationCond cond,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Slice<AccommodationResponse> response = accommodationSearchService.searchAccommodations(cond, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "숙소 목록 검색 성공", response));
    }

    @GetMapping("/{accommodationId}")
    public ResponseEntity<GlobalResponse<GetAccommodationCacheResponse>> getAccommodation(@PathVariable Long accommodationId, HttpServletRequest request) {

        String ipAddress = request.getRemoteAddr();

        GetAccommodationCacheResponse response = accommodationService.getAccommodation(accommodationId, ipAddress);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "숙소 조회 성공", response));
    }

    @GetMapping("/views/ranking")
    public ResponseEntity<GlobalResponse<List<GetAccommodationCacheResponse>>> findAccommodationTodayTop10() {

        List<GetAccommodationCacheResponse> response = accommodationService.findAccommodationTodayTop10();

        return ResponseEntity.ok(GlobalResponse.success(true, "일일 조회수 랭킹 조회 성공", response));
    }
}
