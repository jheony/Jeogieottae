package com.example.jeogieottae.domain.room.dto.response;

import com.example.jeogieottae.domain.room.entity.Room;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoomResponse {

    private final Long id;
    private final String name;
    private final Integer maxGuest;
    private final Long price;
    private final Long discount;
    private final Long totalPrice;
    private final String specialPriceName;

    public static RoomResponse from(Room room) {
        Long price = room.getPrice();
        Long discount = (room.getSpecialPrice() != null) ? room.getSpecialPrice().getDiscount() : 0;
        String specialPriceName = (room.getSpecialPrice() != null) ? room.getSpecialPrice().getName() : null;
        Long totalPrice = price - discount;

        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getMaxGuest(),
                price,
                discount,
                totalPrice,
                specialPriceName
        );
    }
}
