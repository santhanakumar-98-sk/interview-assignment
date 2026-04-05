package com.sk.booking.meeting_room_booking_service.service;
import com.sk.booking.meeting_room_booking_service.dto.RoomUtilizationResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    public List<RoomUtilizationResponse> getRoomUtilization(
            LocalDateTime from,
            LocalDateTime to);

}
