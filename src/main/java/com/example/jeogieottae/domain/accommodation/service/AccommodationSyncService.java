package com.example.jeogieottae.domain.accommodation.service;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.domain.accommodation.document.AccommodationDocument;
import com.example.jeogieottae.domain.accommodation.document.RoomSummary;
import com.example.jeogieottae.domain.accommodation.entity.Accommodation;
import com.example.jeogieottae.domain.accommodation.repository.AccommodationElasticsearchRepository;
import com.example.jeogieottae.domain.accommodation.repository.AccommodationRepository;
import com.example.jeogieottae.domain.reservation.entity.Reservation;
import com.example.jeogieottae.domain.reservation.enums.ReservationStatus;
import com.example.jeogieottae.domain.reservation.repository.ReservationRepository;
import com.example.jeogieottae.domain.room.entity.Room;
import com.example.jeogieottae.domain.room.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.features.search-enabled", havingValue = "true", matchIfMissing = true)
public class AccommodationSyncService {

    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final AccommodationElasticsearchRepository accommodationElasticsearchRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public void syncAllAccommodations() {
        int batchSize = 5000;

        long lastId = 0L;
        long totalCount = 0;

        while (true) {
            Pageable pageable = PageRequest.of(0, batchSize);
            List<Accommodation> accommodationList = accommodationRepository.findByIdGreaterThanOrderByIdAsc(lastId, pageable);

            if (accommodationList.isEmpty()) {
                break;
            }

            List<Long> accommodationIdList = accommodationList.stream().map(Accommodation::getId).toList();
            List<Room> roomList = roomRepository.findByAccommodationIdIn(accommodationIdList);
            List<Long> roomIdList = roomList.stream().map(Room::getId).toList();

            List<ReservationStatus> excludeStatusList = List.of(
                    ReservationStatus.CANCEL,
                    ReservationStatus.VISITED
            );
            List<Reservation> reservationList = reservationRepository.findByRoomIdInAndStatusNotInAndIsDeletedFalse(
                    roomIdList, excludeStatusList
            );

            Map<Long, List<Reservation>> reservationMap = reservationList.stream()
                    .collect(Collectors.groupingBy(reservation -> reservation.getRoom().getId()));

            Map<Long, List<RoomSummary>> roomSummaryMap = roomList.stream()
                    .collect(Collectors.groupingBy(
                            room -> room.getAccommodation().getId(),
                            Collectors.mapping(room -> from(room, reservationMap.getOrDefault(room.getId(), List.of())), Collectors.toList())
                    ));

            List<AccommodationDocument> documents = accommodationList.stream()
                    .map(acc -> from(acc, roomSummaryMap.getOrDefault(acc.getId(), List.of())))
                    .toList();

            accommodationElasticsearchRepository.saveAll(documents);

            lastId = accommodationList.get(accommodationList.size() - 1).getId();
            totalCount += accommodationList.size();

            entityManager.clear();
        }
    }

    @CacheEvict(value = "accommodationSearch", allEntries = true)
    @Transactional(readOnly = true)
    public void syncAccommodation(Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOMMODATION_NOT_FOUND));

        List<Room> roomList = roomRepository.findByAccommodationId(accommodationId);
        List<Long> roomIdList = roomList.stream().map(Room::getId).toList();

        List<ReservationStatus> excludeStatusList = List.of(
                ReservationStatus.CANCEL,
                ReservationStatus.VISITED
        );
        List<Reservation> reservations = reservationRepository.findByRoomIdInAndStatusNotInAndIsDeletedFalse(
                roomIdList, excludeStatusList
        );

        Map<Long, List<Reservation>> reservationMap = reservations.stream()
                .collect(Collectors.groupingBy(reservation -> reservation.getRoom().getId()));

        List<RoomSummary> roomSummaries = roomList.stream()
                .map(room -> from(room, reservationMap.getOrDefault(room.getId(), List.of())))
                .collect(Collectors.toList());

        AccommodationDocument document = from(accommodation, roomSummaries);

        accommodationElasticsearchRepository.save(document);
    }

    private AccommodationDocument from(Accommodation accommodation, List<RoomSummary> roomSummarieList) {
        return new AccommodationDocument(
                accommodation.getId(),
                accommodation.getName(),
                accommodation.getType(),
                accommodation.getLocation(),
                accommodation.getRating(),
                accommodation.getViewCount(),
                roomSummarieList
        );
    }

    private RoomSummary from(Room room, List<Reservation> reservations) {
        List<String> reservedDates = reservations.stream()
                .flatMap(res -> res.getCheckIn().toLocalDate().datesUntil(res.getCheckOut().toLocalDate()))
                .map(LocalDate::toString)
                .distinct()
                .toList();

        return new RoomSummary(
                room.getId(),
                room.getPrice(),
                room.getMaxGuest(),
                reservedDates
        );
    }
}