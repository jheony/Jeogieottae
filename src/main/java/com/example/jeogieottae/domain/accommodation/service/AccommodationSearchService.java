package com.example.jeogieottae.domain.accommodation.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.example.jeogieottae.domain.accommodation.document.AccommodationDocument;
import com.example.jeogieottae.domain.accommodation.document.RoomSummary;
import com.example.jeogieottae.domain.accommodation.dto.condition.SearchAccommodationCond;
import com.example.jeogieottae.domain.accommodation.dto.response.AccommodationResponse;
import com.example.jeogieottae.domain.accommodation.enums.AccommodationSortType;
import com.example.jeogieottae.domain.accommodation.enums.City;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.features.search-enabled", havingValue = "true", matchIfMissing = true)
public class AccommodationSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    @Cacheable(value = "accommodationSearch",
            key = "#cond.toString() + #pageable.pageNumber",
            unless = "#result.isEmpty()")
    public Slice<AccommodationResponse> searchAccommodations(SearchAccommodationCond cond, Pageable pageable) {

        applyCityFromKeyword(cond);

        List<String> targetDateList = getTargetDateList(cond);

        Query roomConditionQuery = createRoomBoolQuery(cond, targetDateList);

        Query finalQuery = Query.of(queryBuilder -> queryBuilder.bool(boolQueryBuilder -> boolQueryBuilder
                .filter(matchNameOrLocation(cond.getKeyword()))
                .filter(eqLocation(cond.getLocate()))
                .filter(eqType(cond.getType()))
                .filter(nestedQueryBuilder -> nestedQueryBuilder.nested(nestedQuery -> nestedQuery
                        .path("rooms")
                        .query(roomConditionQuery)
                ))
        ));

        Pageable executionPageable = Pageable.ofSize(pageable.getPageSize() + 1).withPage(pageable.getPageNumber());

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(finalQuery)
                .withSort(buildSortOptions(cond.getSortBy(), roomConditionQuery))
                .withPageable(executionPageable)
                .build();

        SearchHits<AccommodationDocument> searchHits = elasticsearchOperations.search(nativeQuery, AccommodationDocument.class);

        List<AccommodationDocument> contentDocList = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        boolean hasNext = false;
        if (contentDocList.size() > pageable.getPageSize()) {
            contentDocList.remove(pageable.getPageSize());
            hasNext = true;
        }

        List<AccommodationResponse> content = contentDocList.stream()
                .map(doc -> convertToResponse(doc, cond, targetDateList))
                .collect(Collectors.toList());

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private void applyCityFromKeyword(SearchAccommodationCond cond) {
        if (!StringUtils.hasText(cond.getKeyword()) || cond.getLocate() != null) {
            return;
        }

        String keyword = cond.getKeyword().trim();

        Arrays.stream(City.values())
                .filter(city -> isCityMatch(city, keyword))
                .findFirst()
                .ifPresent(cond::setLocate);
    }

    private boolean isCityMatch(City city, String keyword) {
        return city.name().equalsIgnoreCase(keyword);
    }

    private Query matchNameOrLocation(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return QueryBuilders.matchAll().build()._toQuery();
        }
        return Query.of(queryBuilder -> queryBuilder.bool(boolQueryBuilder -> boolQueryBuilder
                .should(shouldQueryBuilder -> shouldQueryBuilder.match(matchQueryBuilder -> matchQueryBuilder
                        .field("name")
                        .query(keyword)
                ))
                .should(shouldQueryBuilder -> shouldQueryBuilder.match(matchQueryBuilder -> matchQueryBuilder
                        .field("location")
                        .query(keyword)
                ))
        ));
    }

    private Query eqLocation(City locate) {
        if (locate == null) {
            return QueryBuilders.matchAll().build()._toQuery();
        }
        return Query.of(queryBuilder -> queryBuilder.term(termQueryBuilder -> termQueryBuilder
                .field("location")
                .value(FieldValue.of(locate.name()))
        ));
    }

    private Query eqType(Enum<?> type) {
        if (type == null) {
            return QueryBuilders.matchAll().build()._toQuery();
        }
        return Query.of(queryBuilder -> queryBuilder.term(termQueryBuilder -> termQueryBuilder
                .field("type")
                .value(FieldValue.of(type.name()))
        ));
    }

    private Query createRoomBoolQuery(SearchAccommodationCond cond, List<String> targetDateList) {
        Query priceQuery = new RangeQuery.Builder()
                .number(numberRangeQueryBuilder -> {
                    numberRangeQueryBuilder.field("rooms.price");
                    if (cond.getMinPrice() != null) {
                        numberRangeQueryBuilder.gte((double) cond.getMinPrice());
                    }
                    if (cond.getMaxPrice() != null) {
                        numberRangeQueryBuilder.lte((double) cond.getMaxPrice());
                    }
                    return numberRangeQueryBuilder;
                })
                .build()
                ._toQuery();

        Query guestQuery = new RangeQuery.Builder()
                .number(numberRangeQueryBuilder -> {
                    numberRangeQueryBuilder.field("rooms.maxGuest");
                    if (cond.getGuest() != null) {
                        numberRangeQueryBuilder.gte((double) cond.getGuest());
                    }
                    return numberRangeQueryBuilder;
                })
                .build()
                ._toQuery();

        List<FieldValue> dateValueList = targetDateList.stream()
                .map(FieldValue::of)
                .collect(Collectors.toList());

        Query dateFilterQuery = new TermsQuery.Builder()
                .field("rooms.reservedDates")
                .terms(termsQueryFieldBuilder -> termsQueryFieldBuilder.value(dateValueList))
                .build()
                ._toQuery();

        return new BoolQuery.Builder()
                .filter(priceQuery)
                .filter(guestQuery)
                .mustNot(dateFilterQuery)
                .build()
                ._toQuery();
    }

    private List<SortOptions> buildSortOptions(AccommodationSortType sortType, Query roomFilterQuery) {
        List<SortOptions> sortOptionList = new ArrayList<>();

        if (sortType == null) {
            sortOptionList.add(SortOptions.of(sortOptionsBuilder -> sortOptionsBuilder
                    .field(fieldSortBuilder -> fieldSortBuilder
                            .field("viewCount")
                            .order(SortOrder.Desc))));
            return sortOptionList;
        }

        switch (sortType) {
            case LOW_PRICE -> sortOptionList.add(SortOptions.of(sortOptionsBuilder -> sortOptionsBuilder
                    .field(fieldSortBuilder -> fieldSortBuilder
                            .field("rooms.price")
                            .order(SortOrder.Asc)
                            .nested(nestedSortValueBuilder -> nestedSortValueBuilder
                                    .path("rooms")
                                    .filter(roomFilterQuery)
                            )
                    )));
            case HIGH_PRICE -> sortOptionList.add(SortOptions.of(sortOptionsBuilder -> sortOptionsBuilder
                    .field(fieldSortBuilder -> fieldSortBuilder
                            .field("rooms.price")
                            .order(SortOrder.Desc)
                            .nested(nestedSortValueBuilder -> nestedSortValueBuilder
                                    .path("rooms")
                                    .filter(roomFilterQuery)
                            )
                    )));
            case RATING_DESC -> sortOptionList.add(SortOptions.of(sortOptionsBuilder -> sortOptionsBuilder
                    .field(fieldSortBuilder -> fieldSortBuilder
                            .field("rating")
                            .order(SortOrder.Desc))));
            case RATING_ASC -> sortOptionList.add(SortOptions.of(sortOptionsBuilder -> sortOptionsBuilder
                    .field(fieldSortBuilder -> fieldSortBuilder
                            .field("rating")
                            .order(SortOrder.Asc))));
            default -> sortOptionList.add(SortOptions.of(sortOptionsBuilder -> sortOptionsBuilder
                    .field(fieldSortBuilder -> fieldSortBuilder
                            .field("viewCount")
                            .order(SortOrder.Desc))));
        }

        sortOptionList.add(SortOptions.of(sortOptionsBuilder -> sortOptionsBuilder
                .field(fieldSortBuilder -> fieldSortBuilder
                        .field("_score")
                        .order(SortOrder.Desc))));
        return sortOptionList;
    }

    private List<String> getTargetDateList(SearchAccommodationCond cond) {
        LocalDate start = cond.getStartDate() != null ? cond.getStartDate().toLocalDate() : LocalDate.now();
        LocalDate end = cond.getEndDate() != null ? cond.getEndDate().toLocalDate() : start.plusDays(1);

        if (end.isBefore(start) || end.isEqual(start)) {
            end = start.plusDays(1);
        }

        return start.datesUntil(end)
                .map(LocalDate::toString)
                .collect(Collectors.toList());
    }

    private AccommodationResponse convertToResponse(AccommodationDocument doc, SearchAccommodationCond cond, List<String> targetDateList) {
        List<RoomSummary> availableRoomList = doc.getRooms().stream()
                .filter(room -> isAvailableRoom(room, cond, targetDateList))
                .toList();

        long minPrice = availableRoomList.stream()
                .mapToLong(RoomSummary::getPrice)
                .min()
                .orElse(0L);

        Long availableCount = (long) availableRoomList.size();

        return AccommodationResponse.of(
                doc.getName(),
                minPrice,
                doc.getRating(),
                availableCount
        );
    }

    private boolean isAvailableRoom(RoomSummary room, SearchAccommodationCond cond, List<String> targetDateList) {
        if (cond.getMinPrice() != null && room.getPrice() < cond.getMinPrice()) {
            return false;
        }
        if (cond.getMaxPrice() != null && room.getPrice() > cond.getMaxPrice()) {
            return false;
        }
        if (cond.getGuest() != null && room.getMaxGuest() < cond.getGuest()) {
            return false;
        }

        for (String date : targetDateList) {
            if (room.getReservedDates().contains(date)) {
                return false;
            }
        }
        return true;
    }
}
