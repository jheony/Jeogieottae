package com.example.jeogieottae.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUser {

    private final Long userId;
    private final String email;
    private final String username;

    public static AuthUser of(Long userId, String email, String username) {
        return new AuthUser(userId, email, username);
    }
}