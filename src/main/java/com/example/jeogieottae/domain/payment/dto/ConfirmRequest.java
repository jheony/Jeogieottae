package com.example.jeogieottae.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConfirmRequest {
    private String orderId;
    private Long amount;
    private String paymentKey;
}
