package com.example.jeogieottae.domain.accommodation.dto.response;

import com.example.jeogieottae.domain.accommodation.entity.Accommodation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccommodationResponse {

    private final String name;
    private final Long pricePerNight;
    private final double rating;
    private final Long availableRooms;

    public static AccommodationResponse from(Accommodation accommodation, Long pricePerNight, Long availableRooms) {

        return new AccommodationResponse(
                accommodation.getName(),
                pricePerNight,
                accommodation.getRating(),
                availableRooms
        );
    }
}
