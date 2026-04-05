package com.sk.booking.meeting_room_booking_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name="rooms" ,
       uniqueConstraints = {
            @UniqueConstraint(name="uk_room_name", columnNames = "name")
       }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="capacity", nullable = false)
    private Integer capacity;

    @Column(name="floor")
    private Integer floor;

    @ElementCollection
    @CollectionTable(
            name = "room_amenities",
            joinColumns = @JoinColumn(name="room_id")

    )
    @Column(name = "amenity")
   private List<String> amenities;

 }
