package com.example.jeogieottae.domain.payment.controller;

import com.example.jeogieottae.common.dto.AuthUser;
import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.payment.dto.ConfirmRequest;
import com.example.jeogieottae.domain.payment.dto.FailPaymentRequest;
import com.example.jeogieottae.domain.payment.dto.RequestPaymentResponse;
import com.example.jeogieottae.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    @Value("${spring.payment.secret-key}")
    private String secretKey;

    @Value("${spring.payment.base-url}")
    private String baseUrl;

    private final PaymentService paymentService;

    @PostMapping("/request")
    public ResponseEntity<GlobalResponse<RequestPaymentResponse>> requestPayment(@AuthenticationPrincipal AuthUser authUser, @RequestParam Long reservationId) {

        RequestPaymentResponse response = paymentService.requestPayment(authUser.getUserId(), reservationId);
        return ResponseEntity.ok(GlobalResponse.success(true, "결제 요청 완료", response));
    }

    @GetMapping("/success")
    public String successPayment(@ModelAttribute ConfirmRequest request) {

        paymentService.successPayment(request);

        return "redirect:/success.html";
    }

    @GetMapping("/fail")
    public String failPayment(@ModelAttribute FailPaymentRequest request) {

        return "redirect:/fail.html";
    }
}
