package com.example.jeogieottae.domain.specialprice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "special_prices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecialPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "discount", nullable = false)
    private Long discount;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
