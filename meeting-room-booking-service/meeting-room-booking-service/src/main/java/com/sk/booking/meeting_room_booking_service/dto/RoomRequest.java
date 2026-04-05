package com.sk.booking.meeting_room_booking_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.*;

@Data
public class RoomRequest {

    @NotBlank
    private String name;

    @Min(1)
    private Integer capacity;

    private Integer floor;

    private List<String> amenities;
}

