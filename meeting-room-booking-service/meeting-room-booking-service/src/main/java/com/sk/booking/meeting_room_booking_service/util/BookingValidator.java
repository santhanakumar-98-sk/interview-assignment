package com.sk.booking.meeting_room_booking_service.util;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.sk.booking.meeting_room_booking_service.dto.BookingRequest;
import com.sk.booking.meeting_room_booking_service.entity.Booking;
import com.sk.booking.meeting_room_booking_service.entity.Room;
import com.sk.booking.meeting_room_booking_service.exception.BadRequestException;
import com.sk.booking.meeting_room_booking_service.exception.NotFoundException;
import com.sk.booking.meeting_room_booking_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingValidator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final RoomRepository roomRepository;

    public void validateBookingRequest(BookingRequest request) {
        LocalDateTime start;
        LocalDateTime end;

        try {
            start = LocalDateTime.parse(request.getStartTime(), FORMATTER);
            end = LocalDateTime.parse(request.getEndTime(), FORMATTER);
        } catch (Exception e) {
            throw new BadRequestException("startTime/endTime format must be yyyy-MM-dd'T'HH:mm");
        }

        if (!start.isBefore(end)) {
            throw new BadRequestException("startTime must be before endTime");
        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Cannot book past time");
        }

        Duration duration = Duration.between(start, end);
        if (duration.toMinutes() < 15) {
            throw new BadRequestException("Booking duration must be at least 15 minutes");
        }

        if (duration.toHours() > 4 || (duration.toHours() == 4 && duration.toMinutesPart() > 0)) {
            throw new BadRequestException("Booking duration cannot exceed 4 hours");
        }

        DayOfWeek day= start.getDayOfWeek();
        if(day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY){
            throw new BadRequestException("Bookings allowed only Mon-Fri");
        }

        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();
        if(startTime.isBefore(LocalTime.of(8,0)) ||
                endTime.isAfter(LocalTime.of(20,0))){
            throw new BadRequestException("Bookings allowed only between 8:00-20:00");
        }

    }

    public Room resolveRoom(BookingRequest request) {
        if (request.getRoomId() != null) {
            return roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new NotFoundException("Room ID not found"));
        } else if (request.getRoomName() != null && !request.getRoomName().isEmpty()) {
            return roomRepository.findByName(request.getRoomName())
                    .orElseThrow(() -> new NotFoundException("Room name not found"));
        } else {
            throw new BadRequestException("Either roomId or roomName must be provided");
        }
    }

    public void cancellation(Booking booking) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = booking.getStartTime();

        if(!now.isBefore(start)){
            throw new BadRequestException("Cannot cancel a booking " +
                    "that has already started or completed");
        }

        if(now.isAfter(start.minusHours(1))){
            throw new BadRequestException("Booking can only be " +
                    "cancelled up to 1hour before start time");
        }
    }
}