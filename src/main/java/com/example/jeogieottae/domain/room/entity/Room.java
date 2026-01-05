package com.example.jeogieottae.domain.room.entity;

import com.example.jeogieottae.domain.accommodation.entity.Accommodation;
import com.example.jeogieottae.domain.specialprice.entity.SpecialPrice;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(name = "max_guest", nullable = false)
    private Integer maxGuest = 2;

    @ManyToOne(fetch = FetchType.LAZY, optional=false)
    @JoinColumn(name="accommodation_id")
    private Accommodation accommodation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "special_price_id")
    private SpecialPrice specialPrice;

}
