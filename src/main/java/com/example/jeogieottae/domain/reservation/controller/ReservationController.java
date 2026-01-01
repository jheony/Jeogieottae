package com.example.jeogieottae.domain.reservation.controller;

import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.reservation.dto.CreateReseravtionRequest;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationResponse;
import com.example.jeogieottae.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/{roomId}/{userId}")
    public ResponseEntity<GlobalResponse<CreateReservationResponse>> createReservation(
            @PathVariable Long roomId,
            @PathVariable Long userId,
            @ModelAttribute CreateReseravtionRequest request
    ) {
        request.setRoomId(roomId);
        request.setUserId(userId);
        return ResponseEntity.ok(GlobalResponse.success(true, "생성 완", reservationService.createReservation(request)));
    }
}
