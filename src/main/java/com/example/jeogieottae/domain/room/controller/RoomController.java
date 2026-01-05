package com.example.jeogieottae.domain.room.controller;

import com.example.jeogieottae.common.response.GlobalResponse;
import com.example.jeogieottae.domain.room.dto.response.RoomResponse;
import com.example.jeogieottae.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{accommodationId}")
    public ResponseEntity<GlobalResponse<List<RoomResponse>>> getRoomList(@PathVariable Long accommodationId) {

        List<RoomResponse> result = roomService.getRoomList(accommodationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "방 목록 조회 성공", result));
    }
}
