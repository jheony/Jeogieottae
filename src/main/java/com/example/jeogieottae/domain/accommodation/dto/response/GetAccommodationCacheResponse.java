package com.example.jeogieottae.domain.accommodation.dto.response;

import com.example.jeogieottae.domain.accommodation.entity.Accommodation;
import com.example.jeogieottae.domain.accommodation.enums.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAccommodationCacheResponse {

    private String name;
    private City location;
    private double rating;
    private Long viewCount;

    public static GetAccommodationCacheResponse from(Accommodation accommodation) {

        return new GetAccommodationCacheResponse(
                accommodation.getName(),
                accommodation.getLocation(),
                accommodation.getRating(),
                accommodation.getViewCount()
        );
    }
}
