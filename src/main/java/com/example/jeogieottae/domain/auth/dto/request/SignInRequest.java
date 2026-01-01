package com.example.jeogieottae.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
