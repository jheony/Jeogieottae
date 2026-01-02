package com.example.jeogieottae.domain.accommodation.dto.response;

import com.example.jeogieottae.domain.accommodation.entity.Accommodation;
import com.example.jeogieottae.domain.accommodation.enums.AccommodationType;
import com.example.jeogieottae.domain.accommodation.enums.City;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccommodationDto {
    private Long id;
    private String name;
    private AccommodationType type;
    private City location;
    private double rating;

    public static AccommodationDto from(Accommodation accommodation) {

        return new AccommodationDto(
                accommodation.getId(),
                accommodation.getName(),
                accommodation.getType(),
                accommodation.getLocation(),
                accommodation.getRating()
        );
    }
}
