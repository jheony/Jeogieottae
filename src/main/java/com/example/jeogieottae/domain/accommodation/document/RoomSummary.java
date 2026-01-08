package com.example.jeogieottae.domain.accommodation.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomSummary {

    @Field(type = FieldType.Long)
    private Long roomId;

    @Field(type = FieldType.Long)
    private Long price;

    @Field(type = FieldType.Integer)
    private Integer maxGuest;

    @Field(type = FieldType.Keyword)
    private List<String> reservedDates;
}