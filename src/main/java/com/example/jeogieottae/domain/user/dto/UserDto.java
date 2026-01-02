package com.example.jeogieottae.domain.user.dto;

import com.example.jeogieottae.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String username;
    private boolean isDeleted;

    public static UserDto from(User user) {

        return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        false
        );
    }

}
