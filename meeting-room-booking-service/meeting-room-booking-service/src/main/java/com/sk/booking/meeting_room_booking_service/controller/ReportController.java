package com.sk.booking.meeting_room_booking_service.controller;

import com.sk.booking.meeting_room_booking_service.dto.RoomUtilizationResponse;
import com.sk.booking.meeting_room_booking_service.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/room-utilization")
    public List<RoomUtilizationResponse> getReport(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) {
        return reportService.getRoomUtilization(from, to);
    }
}
