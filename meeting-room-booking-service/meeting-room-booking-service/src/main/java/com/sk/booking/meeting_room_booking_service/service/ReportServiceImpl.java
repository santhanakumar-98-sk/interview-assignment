package com.sk.booking.meeting_room_booking_service.service;

import com.sk.booking.meeting_room_booking_service.dto.RoomUtilizationResponse;
import com.sk.booking.meeting_room_booking_service.entity.Booking;
import com.sk.booking.meeting_room_booking_service.entity.Room;
import com.sk.booking.meeting_room_booking_service.exception.BadRequestException;
import com.sk.booking.meeting_room_booking_service.repository.BookingRepository;
import com.sk.booking.meeting_room_booking_service.repository.RoomRepository;
import com.sk.booking.meeting_room_booking_service.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public List<RoomUtilizationResponse> getRoomUtilization(
            LocalDateTime from,
            LocalDateTime to) {

        if (!from.isBefore(to)) {
            throw new BadRequestException("Invalid date range");
        }

        List<Booking> bookings =
                bookingRepository.findBookingsInRange(from, to);

        Map<Room, Double> roomHours = new HashMap<>();

        for (Booking b : bookings) {

            double overlap = TimeUtil.calculateOverlapHours(
                    b.getStartTime(),
                    b.getEndTime(),
                    from,
                    to
            );

            roomHours.merge(b.getRoom(), overlap, Double::sum);
        }

        double businessHours = TimeUtil.calculateBusinessHours(from, to);

        List<Room> rooms = roomRepository.findAll();

        List<RoomUtilizationResponse> result = new ArrayList<>();

        for (Room room : rooms) {

            double booked = roomHours.getOrDefault(room, 0.0);

            double utilization = (businessHours == 0)
                    ? 0
                    : booked / businessHours;

            result.add(RoomUtilizationResponse.builder()
                    .roomId(room.getId())
                    .roomName(room.getName())
                    .totalBookingHours(booked)
                    .utilizationPercent(utilization)
                    .build());
        }

        return result;
    }

}
