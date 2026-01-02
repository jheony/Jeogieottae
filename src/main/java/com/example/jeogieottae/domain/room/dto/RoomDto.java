package com.example.jeogieottae.domain.room.dto;

import com.example.jeogieottae.domain.accommodation.dto.response.AccommodationDto;
import com.example.jeogieottae.domain.room.entity.Room;
import com.example.jeogieottae.domain.specialprice.entity.SpecialPrice;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomDto {
    private Long id;
    private String name;
    private Long price;
    private AccommodationDto accommodation;
    private SpecialPrice specialPrice;

    public static RoomDto from(Room room) {

        return new RoomDto(
                room.getId(),
                room.getName(),
                room.getPrice(),
                AccommodationDto.from(room.getAccommodation()),
                room.getSpecialPrice()
        );
    }
}
