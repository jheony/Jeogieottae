package com.example.jeogieottae.domain.room.service;

import com.example.jeogieottae.domain.room.dto.response.RoomResponse;
import com.example.jeogieottae.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomList(Long accommodationId) {

        return roomRepository.findAllByAccommodationIdOrderByPriceAsc(accommodationId)
                .stream()
                .map(RoomResponse::from)
                .collect(Collectors.toList());
    }
}
