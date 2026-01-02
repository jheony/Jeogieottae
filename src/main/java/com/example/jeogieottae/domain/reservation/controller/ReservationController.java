package com.example.jeogieottae.domain.reservation.controller;

import com.example.jeogieottae.common.dto.AuthUser;
import com.example.jeogieottae.common.response.CustomPageResponse;
import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationRequest;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationResponse;
import com.example.jeogieottae.domain.reservation.dto.ReservationResponse;
import com.example.jeogieottae.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/{roomId}/{couponId}")
    public ResponseEntity<GlobalResponse<CreateReservationResponse>> createReservation(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long roomId,
            @PathVariable Long couponId,
            @ModelAttribute CreateReservationRequest request
    ) {
        CreateReservationResponse response = reservationService.createReservation(roomId, couponId, authUser.getUserId(), request);
        return ResponseEntity.ok(GlobalResponse.success(true, "숙소 예약 성공", response));
    }

    @GetMapping
    public ResponseEntity<CustomPageResponse<ReservationResponse>> getAllMyReservationList(

            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        CustomPageResponse<ReservationResponse> response = reservationService.getAllMyReservation(authUser.getUserId(), pageable);
        return ResponseEntity.ok(response);
    }
}
