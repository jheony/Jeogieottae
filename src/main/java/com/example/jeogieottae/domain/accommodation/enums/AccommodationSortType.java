package com.example.jeogieottae.domain.accommodation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccommodationSortType {

    VIEW_COUNT_DESC("조회수 높은 순"),
    LOW_PRICE("가격 낮은 순"),
    HIGH_PRICE("가격 높은 순"),
    RATING_DESC("별점 높은 순"),
    RATING_ASC("별점 낮은 순");

    private final String description;
}