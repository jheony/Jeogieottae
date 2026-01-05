package com.example.jeogieottae.domain.accommodation.entity;

import com.example.jeogieottae.domain.accommodation.enums.AccommodationType;
import com.example.jeogieottae.domain.accommodation.enums.City;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "accommodations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccommodationType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private City location;

    @Column(nullable = false)
    private double rating;

    @Setter
    @Column(name = "view_count")
    private Long viewCount;

    public Accommodation(String name, AccommodationType type, City location) {

        this.name = name;
        this.type = type;
        this.location = location;
        this.rating = 0;
        this.viewCount = 0L;
    }
}
