package com.example.jeogieottae.domain.reservation.service;

import com.example.jeogieottae.domain.reservation.dto.CreateReseravtionRequest;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {
    @Transactional
    public CreateReservationResponse createReservation(CreateReseravtionRequest request) {
        return new CreateReservationResponse(null,null,null,null,null,null);
    }
}
