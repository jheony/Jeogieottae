package com.example.jeogieottae.domain.accommodation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccommodationType {

    HOTEL("HOTEL"),
    MOTEL("MOTEL"),
    RESORT("RESORT"),
    POOL_VILLA("POOL_VILLA")
    ;

    private final String typeName;
}
