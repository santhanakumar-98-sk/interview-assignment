package com.sk.booking.meeting_room_booking_service.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class BookingListResponse {
    private List<BookingResponse> bookings;
    private long total;
    private int limit;
    private int offset;

}
