package com.sk.booking.meeting_room_booking_service.dto;
import com.sk.booking.meeting_room_booking_service.util.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private Long roomId;
    private String roomName;
    private String title;
    private BookingStatus status;
    private String organizerEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
