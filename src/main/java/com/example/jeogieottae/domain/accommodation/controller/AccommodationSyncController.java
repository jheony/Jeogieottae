package com.example.jeogieottae.domain.accommodation.controller;

import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.accommodation.service.AccommodationSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.features.search-enabled", havingValue = "true", matchIfMissing = true)
@RequestMapping("/sync")
public class AccommodationSyncController {

    private final AccommodationSyncService accommodationSyncService;

    @PostMapping("/accommodations")
    public ResponseEntity<GlobalResponse<Object>> syncAccommodations() {
        accommodationSyncService.syncAllAccommodations();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "데이터 동기화가 완료되었습니다.", null));
    }
}