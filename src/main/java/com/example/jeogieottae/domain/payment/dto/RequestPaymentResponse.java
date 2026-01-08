package com.example.jeogieottae.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestPaymentResponse {
    private String paymentUrl;

    public static RequestPaymentResponse from(String paymentUrl) {

        return new RequestPaymentResponse(paymentUrl);
    }
}
