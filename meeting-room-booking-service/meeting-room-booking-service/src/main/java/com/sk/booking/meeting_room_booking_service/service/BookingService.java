package com.sk.booking.meeting_room_booking_service.service;

import com.sk.booking.meeting_room_booking_service.dto.BookingListResponse;
import com.sk.booking.meeting_room_booking_service.dto.BookingRequest;
import com.sk.booking.meeting_room_booking_service.dto.BookingResponse;

import java.time.LocalDateTime;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);
    BookingResponse cancelBooking(Long bookingId);
    BookingListResponse getBookings(Long roomId, LocalDateTime from,
                                    LocalDateTime to,
                                    int limit, int offset);
    BookingResponse createBookingWithIdempotency(
            String key,
            BookingRequest request);

}
