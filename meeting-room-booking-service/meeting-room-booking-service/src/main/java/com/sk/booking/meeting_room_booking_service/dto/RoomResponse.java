package com.sk.booking.meeting_room_booking_service.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class RoomResponse {
    private Long id;
    private String name;
    private Integer capacity;
    private Integer floor;
    private List<String> amenities;
}

