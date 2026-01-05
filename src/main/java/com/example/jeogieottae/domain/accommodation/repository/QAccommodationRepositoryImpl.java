package com.example.jeogieottae.domain.accommodation.repository;

import com.example.jeogieottae.domain.accommodation.dto.condition.SearchAccommodationCond;
import com.example.jeogieottae.domain.accommodation.dto.response.AccommodationResponse;
import com.example.jeogieottae.domain.accommodation.enums.AccommodationSortType;
import com.example.jeogieottae.domain.accommodation.enums.AccommodationType;
import com.example.jeogieottae.domain.accommodation.enums.City;
import com.example.jeogieottae.domain.reservation.enums.ReservationStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.jeogieottae.domain.accommodation.entity.QAccommodation.accommodation;
import static com.example.jeogieottae.domain.reservation.entity.QReservation.reservation;
import static com.example.jeogieottae.domain.room.entity.QRoom.room;

@Repository
@RequiredArgsConstructor
public class QAccommodationRepositoryImpl implements QAccommodationRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AccommodationResponse> searchAccommodations(SearchAccommodationCond cond, Pageable pageable) {

        List<AccommodationResponse> contentList = queryFactory
                .select(Projections.constructor(AccommodationResponse.class,
                        accommodation.name,
                        room.price.min(),
                        accommodation.rating,
                        room.count()
                ))
                .from(accommodation)
                .join(room).on(room.accommodation.eq(accommodation))
                .where(
                        containsKeyword(cond.getKeyword()),
                        eqLocation(cond.getLocate()),
                        eqType(cond.getType()),
                        betweenPrice(cond.getMinPrice(), cond.getMaxPrice()),
                        goeMaxGuest(cond.getGuest()),
                        notReserved(cond.getStartDate(), cond.getEndDate())
                )
                .groupBy(accommodation.id)
                .orderBy(createOrderSpecifier(cond.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(accommodation.count())
                .from(accommodation)
                .where(
                        accommodation.id.in(
                                JPAExpressions
                                        .select(room.accommodation.id)
                                        .from(room)
                                        .where(
                                                containsKeyword(cond.getKeyword()),
                                                eqLocation(cond.getLocate()),
                                                eqType(cond.getType()),
                                                betweenPrice(cond.getMinPrice(), cond.getMaxPrice()),
                                                goeMaxGuest(cond.getGuest()),
                                                notReserved(cond.getStartDate(), cond.getEndDate())
                                        )
                        )
                )
                .fetchOne();

        return new PageImpl<>(contentList, pageable, totalCount != null ? totalCount : 0);
    }


    private OrderSpecifier<?> createOrderSpecifier(AccommodationSortType sortType) {

        if (sortType == null) {
            return accommodation.viewCount.desc();
        }

        return switch (sortType) {
            case LOW_PRICE -> room.price.min().asc();
            case HIGH_PRICE -> room.price.min().desc();
            case RATING_DESC -> accommodation.rating.desc();
            case RATING_ASC -> accommodation.rating.asc();
            default -> accommodation.viewCount.desc();
        };
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return accommodation.name.containsIgnoreCase(keyword)
                .or(accommodation.location.stringValue().containsIgnoreCase(keyword));
    }

    private BooleanExpression eqLocation(City locate) {
        return locate != null ? accommodation.location.eq(locate) : null;
    }

    private BooleanExpression eqType(AccommodationType type) {
        return type != null ? accommodation.type.eq(type) : null;
    }

    private BooleanExpression betweenPrice(Long minPrice, Long maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return room.price.between(minPrice, maxPrice);
        }
        if (minPrice != null) {
            return room.price.goe(minPrice);
        }
        if (maxPrice != null) {
            return room.price.loe(maxPrice);
        }
        return null;
    }

    private BooleanExpression goeMaxGuest(Integer guest) {
        return guest != null ? room.maxGuest.goe(guest) : null;
    }

    private BooleanExpression notReserved(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }

        return JPAExpressions
                .selectOne()
                .from(reservation)
                .where(
                        reservation.room.eq(room),
                        reservation.isDeleted.isFalse(),
                        reservation.status.ne(ReservationStatus.CANCEL),
                        reservation.checkIn.lt(endDate),
                        reservation.checkOut.gt(startDate)
                )
                .notExists();
    }
}