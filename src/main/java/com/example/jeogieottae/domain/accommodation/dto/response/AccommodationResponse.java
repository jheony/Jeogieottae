package com.example.jeogieottae.domain.accommodation.dto.response;


import com.example.jeogieottae.domain.accommodation.entity.Accommodation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccommodationResponse {

    private final String name;
    private final int pricePerNight;
    private final double rating;
    private final int availableRooms;

    public static AccommodationResponse from(Accommodation accommodation) {
        return new AccommodationResponse(
                accommodation.getName(),
                0,
                accommodation.getRating(),
                0
        );
    }
}
