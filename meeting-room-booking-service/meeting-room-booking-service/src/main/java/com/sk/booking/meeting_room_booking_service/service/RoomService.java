package com.sk.booking.meeting_room_booking_service.service;

import com.sk.booking.meeting_room_booking_service.dto.RoomRequest;
import com.sk.booking.meeting_room_booking_service.dto.RoomResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoomService {

    RoomResponse createRoom(RoomRequest request);
    List<RoomResponse> getRooms(Integer minCapacity, String amenity);

}
