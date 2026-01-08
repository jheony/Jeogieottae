package com.example.jeogieottae.domain.payment.controller;

import com.example.jeogieottae.domain.payment.dto.ConfirmRequest;
import com.example.jeogieottae.domain.payment.dto.FailPaymentRequest;
import com.example.jeogieottae.domain.payment.dto.PaymentRequest;
import com.example.jeogieottae.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

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
    public ResponseEntity<String> requestPayment(@ModelAttribute PaymentRequest request) {

        WebClient client = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String response = client.post()
                .bodyValue(request)   // 요청 바디에 orderId, orderName, amount, successUrl, failUrl 포함
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public String successPayment(@ModelAttribute ConfirmRequest request) {

        paymentService.successPayment(request);

        return "redirect:/success.html";
    }

    @GetMapping("/fail")
    public ResponseEntity<String> failPayment(@ModelAttribute FailPaymentRequest request) {

        return ResponseEntity.ok(request.getMessage());
    }


    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(@RequestBody ConfirmRequest request) {
        String result = paymentService.successPayment(request);
        return ResponseEntity.ok(result);
    }
}
