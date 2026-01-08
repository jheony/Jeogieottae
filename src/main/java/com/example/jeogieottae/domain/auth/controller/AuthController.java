package com.example.jeogieottae.domain.auth.controller;

import com.example.jeogieottae.common.dto.AuthUser;
import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.auth.dto.request.SignInRequest;
import com.example.jeogieottae.domain.auth.dto.request.SignUpRequest;
import com.example.jeogieottae.domain.auth.dto.response.SignInResponse;
import com.example.jeogieottae.domain.auth.dto.response.SignUpResponse;
import com.example.jeogieottae.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse<SignUpResponse>> signUp(@RequestBody SignUpRequest request) {

        SignUpResponse response = authService.signUp(request);

        return ResponseEntity.ok(GlobalResponse.success(true, "회원가입 성공", response));
    }

    @PostMapping("/signin")
    public ResponseEntity<GlobalResponse<SignInResponse>> signIn(@RequestBody SignInRequest request) {

        SignInResponse response = authService.signIn(request);

        return ResponseEntity.ok(GlobalResponse.success(true, "로그인 성공", response));
    }

    @GetMapping("/me")
    public AuthUser me(@AuthenticationPrincipal AuthUser authUser) {
        if(authUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return authUser;
    }
}
