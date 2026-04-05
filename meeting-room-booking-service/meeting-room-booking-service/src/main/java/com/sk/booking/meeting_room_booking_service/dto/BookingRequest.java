package com.sk.booking.meeting_room_booking_service.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private Long roomId;
    private String roomName;
    private String title;
    private String organizerEmail;
    private String startTime;
    private String endTime;
}
