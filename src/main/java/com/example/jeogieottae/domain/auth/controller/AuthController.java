package com.example.jeogieottae.domain.auth.controller;

import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.auth.dto.request.SignUpRequest;
import com.example.jeogieottae.domain.auth.dto.response.SignUpResponse;
import com.example.jeogieottae.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse<SignUpResponse>> signup(@RequestBody SignUpRequest request) {

        SignUpResponse response = authService.signUp(request);

        return ResponseEntity.ok(GlobalResponse.success(true, "회원가입 성공", response));
    }
}
