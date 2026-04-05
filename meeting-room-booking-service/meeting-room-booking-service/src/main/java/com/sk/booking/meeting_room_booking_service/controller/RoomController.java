package com.sk.booking.meeting_room_booking_service.controller;

import com.sk.booking.meeting_room_booking_service.dto.RoomRequest;
import com.sk.booking.meeting_room_booking_service.dto.RoomResponse;
import com.sk.booking.meeting_room_booking_service.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomService.createRoom(request));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRooms(
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) String amenity) {

        return ResponseEntity.ok(
                roomService.getRooms(minCapacity, amenity)
        );
    }
}

