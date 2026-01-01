package com.example.jeogieottae.domain.reservation.controller;

import com.example.jeogieottae.common.dto.AuthUser;
import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationRequest;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationResponse;
import com.example.jeogieottae.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
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
}
