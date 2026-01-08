package com.example.jeogieottae.domain.accommodation.dto.condition;

import com.example.jeogieottae.domain.accommodation.enums.AccommodationSortType;
import com.example.jeogieottae.domain.accommodation.enums.AccommodationType;
import com.example.jeogieottae.domain.accommodation.enums.City;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SearchAccommodationCond {

    private String keyword;

    private Long minPrice;

    private Long maxPrice;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private City locate;

    private Integer guest;

    private AccommodationType type;

    private AccommodationSortType sortBy;
}
