package com.sk.booking.meeting_room_booking_service.controller;

import com.sk.booking.meeting_room_booking_service.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RoomControllerTest extends BaseTest {

    @Test
    void createRoom() throws Exception {
        String request = """
        {
          "name": "Conf Room A",
          "capacity": 10,
          "floor": 2,
          "amenities": ["TV", "AC", "WIFI"]
        }
        """;

        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    void createRoom_invalid() throws Exception {
        String request = """
    {
      "name": "",
      "capacity": -1
    }
    """;

        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listRooms() throws Exception {
        mockMvc.perform(get("/api/v1/rooms")
                        .param("amenity", "WIFI")
                        .param("minCapacity", "6"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isArray());
    }
}
