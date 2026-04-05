package com.sk.booking.meeting_room_booking_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomUtilizationResponse {
    private Long roomId;
    private String roomName;
    private double totalBookingHours;
    private double utilizationPercent;
}

