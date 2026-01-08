package com.example.jeogieottae.domain.payment.dto;

import lombok.Getter;

@Getter
public class PaymentRequest {
    private String orderId;
    private String orderName;
    private Long amount;
    private String successUrl;
    private String failUrl;
}
