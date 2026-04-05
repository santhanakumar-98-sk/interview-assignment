package com.sk.booking.meeting_room_booking_service.controller;

import com.sk.booking.meeting_room_booking_service.dto.BookingListResponse;
import com.sk.booking.meeting_room_booking_service.dto.BookingRequest;
import com.sk.booking.meeting_room_booking_service.dto.BookingResponse;
import com.sk.booking.meeting_room_booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(

            @RequestHeader(value ="Idempotency-key",required = false)
            String key,

            @RequestBody BookingRequest request) {

        BookingResponse response;

        if(key !=null && !key.isBlank()){
            response = bookingService.createBookingWithIdempotency(key,request);
        } else{
            response = bookingService.createBooking(request);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<BookingListResponse> getBookings(

            @RequestParam(required = false) Long roomId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {

        return ResponseEntity.ok(
                bookingService.getBookings(roomId, from, to, limit, offset)
        );
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancel(@PathVariable Long id) {
       return  ResponseEntity.ok(
            bookingService.cancelBooking(id)
       );
    }



}





