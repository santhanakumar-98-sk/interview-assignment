package com.sk.booking.meeting_room_booking_service.controller;

import com.sk.booking.meeting_room_booking_service.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookingControllerTest extends BaseTest {

    @BeforeEach
    void setupRoom() throws Exception {

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
    }

    @Test
    void createBooking_success() throws Exception {

        String request = """
        {
            "roomId": 1,
            "title": "Team Sync",
            "organizerEmail": "kumar@sk.com",
            "startTime": "2026-04-06T11:00",
            "endTime": "2026-04-06T12:10"
        }
        """;

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createBooking_idempotent() throws Exception {

        String request = """
        {
            "roomId": 1,
            "title": "Team Sync",
            "organizerEmail": "kumar@sk.com",
            "startTime": "2026-04-10T11:00",
            "endTime": "2026-04-10T11:30"
        }
        """;

        mockMvc.perform(post("/api/v1/bookings")
                        .header("Idempotency-key", "booking-user10-room1-20260405")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());
    }

    @Test
    void listBookings() throws Exception {

        mockMvc.perform(get("/api/v1/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void cancelBooking() throws Exception {

        String request = """
        {
            "roomId": 1,
            "title": "Team Sync",
            "organizerEmail": "kumar@sk.com",
            "startTime": "2026-04-06T11:00",
            "endTime": "2026-04-06T12:00"
        }
        """;

        mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));

        mockMvc.perform(post("/api/v1/bookings/{id}/cancel", 1))
                .andExpect(status().isOk());
    }
}
