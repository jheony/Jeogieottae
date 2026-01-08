package com.example.jeogieottae.domain.user.entity;

import com.example.jeogieottae.domain.auth.enums.LoginType;
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

    @Column(length = 200)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private LoginType loginType;

    @Column(length = 50)
    private String provider;

    @Column(length = 100)
    private String providerId;

    @Column
    private boolean isDeleted = false;

    public static User createLocal(String email, String username, String password) {
        User user = new User();
        user.email = email;
        user.username = username;
        user.password = password;
        user.loginType = LoginType.LOCAL;
        return user;
    }

    public static User createKakao(String email, String username, String providerId) {
        User user = new User();
        user.email = email;
        user.username = username;
        user.password = null;
        user.loginType = LoginType.KAKAO;
        user.provider = "kakao";
        user.providerId = providerId;
        return user;
    }
}
