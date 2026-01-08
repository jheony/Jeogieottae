package com.example.jeogieottae.domain.payment.service;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.domain.payment.dto.ConfirmRequest;
import com.example.jeogieottae.domain.reservation.entity.Reservation;
import com.example.jeogieottae.domain.reservation.enums.ReservationPayment;
import com.example.jeogieottae.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${spring.payment.secret-key}")
    private String secretKey;

    @Value("${spring.payment.base-url}")
    private String baseUrl;


    private final ReservationRepository reservationRepository;
    private final WebClient webClient = WebClient.builder().build();

    public String successPayment(ConfirmRequest request) {


        String response = webClient.post()
                .uri(baseUrl + "/confirm")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String[] orderId = request.getOrderId().split("-");

        Reservation reservation = reservationRepository.findById(Long.valueOf(orderId[0])).orElseThrow(
                () -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        if (reservation.getPayment().equals(ReservationPayment.SUCCESS)) {
            throw new CustomException(ErrorCode.PAID_RESERVATION);
        }
        reservation.setPayment(ReservationPayment.SUCCESS);
        reservationRepository.flush();

        return "결제가 완료되었습니다.";
    }
}
