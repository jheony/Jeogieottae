package com.example.jeogieottae.domain.accommodation.dto.response;

import com.example.jeogieottae.domain.accommodation.entity.Accommodation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetAccommodationResponse {

    private final String name;
    private final String location;
    private final double rating;
    private final int availableRooms;
    private final Long viewCount;

    public static GetAccommodationResponse from(Accommodation accommodation) {

        return new GetAccommodationResponse(
                accommodation.getName(),
                accommodation.getLocation(),
                accommodation.getRating(),
                0,
                accommodation.getViewCount()
        );
    }
}
