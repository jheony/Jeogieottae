package com.example.jeogieottae.domain.accommodation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum City {

    SEOUL("SEOUL"),
    ANYANG("ANYANG"),
    GWANGJU("GWANGJU"),
    CHEONAN("CHEONAN"),
    BUSAN("BUSAN");

    private final String cityName;
}
