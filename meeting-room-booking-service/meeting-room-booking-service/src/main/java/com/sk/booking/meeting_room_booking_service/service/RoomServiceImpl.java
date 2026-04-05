package com.sk.booking.meeting_room_booking_service.service;

import com.sk.booking.meeting_room_booking_service.dto.RoomRequest;
import com.sk.booking.meeting_room_booking_service.dto.RoomResponse;
import com.sk.booking.meeting_room_booking_service.entity.Room;
import com.sk.booking.meeting_room_booking_service.exception.BadRequestException;
import com.sk.booking.meeting_room_booking_service.exception.ConflictException;
import com.sk.booking.meeting_room_booking_service.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public RoomResponse createRoom(RoomRequest request) {

        String name = request.getName().trim();

        roomRepository.findByNameIgnoreCase(name)
                .ifPresent(r -> {
                    throw new ConflictException("Room name already exists");
                });

        if (request.getCapacity() == null || request.getCapacity() < 1) {
            throw new BadRequestException("Capacity must be >= 1");
        }

        Room room = Room.builder()
                .name(name)
                .capacity(request.getCapacity())
                .floor(request.getFloor())
                .amenities(request.getAmenities())
                .build();

        room = roomRepository.save(room);

        return map(room);
    }

    @Override
    public List<RoomResponse> getRooms(Integer minCapacity, String amenity) {

        if(minCapacity != null && minCapacity<1){
            throw  new BadRequestException("minCapacity must be >=1");
        }

        List<Room> rooms = roomRepository.searchRooms(minCapacity,amenity);

        return rooms.stream()
                .map(this::map).collect(Collectors.toList());
    }

    private RoomResponse map(Room r) {

        return RoomResponse.builder()
                .id(r.getId())
                .name(r.getName())
                .capacity(r.getCapacity())
                .floor(r.getFloor())
                .amenities(r.getAmenities())
                .build();
    }
}


