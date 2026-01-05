package com.example.jeogieottae.domain.accommodation.service;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.common.response.CustomPageResponse;
import com.example.jeogieottae.domain.accommodation.dto.condition.SearchAccommodationCond;
import com.example.jeogieottae.domain.accommodation.dto.response.AccommodationResponse;
import com.example.jeogieottae.domain.accommodation.dto.response.GetAccommodationCacheResponse;
import com.example.jeogieottae.domain.accommodation.entity.Accommodation;
import com.example.jeogieottae.domain.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(readOnly = true)
    public CustomPageResponse<AccommodationResponse> searchAccommodations(SearchAccommodationCond cond, Pageable pageable) {

        if (cond.getStartDate() == null) {
            cond.setStartDate(LocalDateTime.now());
        }
        if (cond.getEndDate() == null) {
            cond.setEndDate(cond.getStartDate().plusDays(1));
        }

        Page<AccommodationResponse> page = accommodationRepository.searchAccommodations(cond, pageable);
        return CustomPageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public GetAccommodationCacheResponse getAccommodation(Long accommodationId) {

        String key = "accommodation" + accommodationId;

        GetAccommodationCacheResponse response = (GetAccommodationCacheResponse) redisTemplate.opsForValue().get(key);

        if (response == null) {
            Accommodation accommodation = accommodationRepository.findById(accommodationId).orElseThrow(
                    () -> new CustomException(ErrorCode.ACCOMMODATION_NOT_FOUND));

            response = GetAccommodationCacheResponse.from(accommodation);

            redisTemplate.opsForValue().set(key, response);
            redisTemplate.expire(key, 1, TimeUnit.HOURS);
        }

        String dailyKey = "daily_accommodation_views: " + LocalDate.now();
        redisTemplate.opsForZSet().incrementScore(dailyKey, accommodationId, 1);
        redisTemplate.expire(dailyKey, 1, TimeUnit.DAYS);

        String currentKey = "current_accommodation_views";
        redisTemplate.opsForZSet().incrementScore(currentKey, accommodationId, 1);
        redisTemplate.expire(dailyKey, 1, TimeUnit.DAYS);

        return response;
    }

    @Scheduled(cron = "0 * * * * *")
    public void syncViewCountsToDatabase() {

        String accommodationKey = "current_accommodation_views";

        Set<Long> accommodationIds = redisTemplate.opsForZSet().range(accommodationKey, 0, -1)
                .stream()
                .map(id -> Long.parseLong(id.toString()))
                .collect(Collectors.toSet());

        if (accommodationIds.isEmpty()) {
            return;
        }

        for (Long accommodationId : accommodationIds) {
            Double score = redisTemplate.opsForZSet().score(accommodationKey, accommodationId);

            if (score != null && score > 0) {

                Accommodation accommodation = accommodationRepository.findById(accommodationId).orElse(null);
                if (accommodation != null) {
                    accommodation.setViewCount(accommodation.getViewCount() + score.intValue());
                    accommodationRepository.save(accommodation);
                }
            }
            redisTemplate.opsForZSet().remove(accommodationKey, accommodationId);
        }
    }

}
