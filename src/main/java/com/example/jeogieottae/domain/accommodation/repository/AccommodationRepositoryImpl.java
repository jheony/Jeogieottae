package com.example.jeogieottae.domain.accommodation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

public class AccommodationRepositoryImpl implements AccommodationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AccommodationRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

}
