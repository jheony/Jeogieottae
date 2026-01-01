package com.example.jeogieottae.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String username;

    @Column(length = 200, nullable = false)
    private String password;

    @Column
    private boolean isDeleted = false;

    private User(String email, String username, String password) {

        this.email = email;
        this.username = username;
        this.password = password;
    }

    public static User create(String email, String username, String password) {

        return new User(email, username, password);
    }
}
