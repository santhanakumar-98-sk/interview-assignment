package com.sk.booking.meeting_room_booking_service.controller;

import com.sk.booking.meeting_room_booking_service.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReportControllerTest extends BaseTest {

    @BeforeEach
    void setupData() throws Exception {

        String room = """
        {
          "name": "Conf Room %d",
          "capacity": 10,
          "floor": 2,
          "amenities": ["WIFI"]
        }
        """.formatted(System.currentTimeMillis());

        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(room))
                .andExpect(status().isCreated());

        String booking = """
        {
            "roomId": 1,
            "title": "Team Sync",
            "organizerEmail": "kumar@sk.com",
            "startTime": "2026-04-07T18:00",
            "endTime": "2026-04-07T20:00"
        }
        """;

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(booking))
                .andExpect(status().isCreated());
    }

    @Test
    void roomUtilizationReport() throws Exception {

        mockMvc.perform(get("/api/v1/reports/room-utilization")
                        .param("from", "2026-04-01T10:00")
                        .param("to", "2026-04-10T10:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }
}