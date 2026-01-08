package com.example.jeogieottae.domain.accommodation.controller;

import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.accommodation.dto.condition.SearchAccommodationCond;
import com.example.jeogieottae.domain.accommodation.dto.response.AccommodationResponse;
import com.example.jeogieottae.domain.accommodation.dto.response.GetAccommodationCacheResponse;
import com.example.jeogieottae.domain.accommodation.service.AccommodationSearchService;
import com.example.jeogieottae.domain.accommodation.service.AccommodationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@ConditionalOnProperty(name = "app.features.search-enabled", havingValue = "true", matchIfMissing = true)
@RequestMapping("/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    private final Optional<AccommodationSearchService> accommodationSearchService;

    private final boolean searchEnabled;

    public AccommodationController(
            AccommodationService accommodationService,
            Optional<AccommodationSearchService> accommodationSearchService,
            @Value("${app.features.search-enabled:true}") boolean searchEnabled
    ) {
        this.accommodationService = accommodationService;
        this.accommodationSearchService = accommodationSearchService;
        this.searchEnabled = searchEnabled;
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<Slice<AccommodationResponse>>> searchAccommodations(
            @ModelAttribute SearchAccommodationCond cond,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        if (!searchEnabled) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "검색 기능은 현재 비활성화되어 있습니다."
            );
        }

        AccommodationSearchService searchService = accommodationSearchService.orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "검색 서비스가 준비되지 않았습니다."
                )
        );

        Slice<AccommodationResponse> response = searchService.searchAccommodations(cond, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "숙소 목록 검색 성공", response));
    }

    @GetMapping("/{accommodationId}")
    public ResponseEntity<GlobalResponse<GetAccommodationCacheResponse>> getAccommodation(
            @PathVariable Long accommodationId,
            HttpServletRequest request
    ) {
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
