package com.example.jeogieottae.domain.accommodation.repository;

import com.example.jeogieottae.domain.accommodation.document.AccommodationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AccommodationElasticsearchRepository extends ElasticsearchRepository<AccommodationDocument, Long> {
}