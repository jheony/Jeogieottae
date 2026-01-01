package com.example.jeogieottae.common.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthUser {

    private final Long userId;
    private final String email;
    private final String username;

    public static AuthUser create(Long userId, String email, String username) {
        return new AuthUser(userId, email, username);
    }
}
