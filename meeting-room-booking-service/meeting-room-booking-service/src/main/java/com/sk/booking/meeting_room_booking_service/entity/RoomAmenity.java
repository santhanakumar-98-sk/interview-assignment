package com.sk.booking.meeting_room_booking_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Data;

//@Entity
@Data
@Table(name = "room_amenities")
public class RoomAmenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amenity")
    private String name;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
