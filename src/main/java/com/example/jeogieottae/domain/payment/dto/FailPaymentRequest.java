package com.example.jeogieottae.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FailPaymentRequest {
    private String code;
    private String message;
}
