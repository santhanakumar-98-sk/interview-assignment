package com.sk.booking.meeting_room_booking_service.entity;

import com.sk.booking.meeting_room_booking_service.util.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @NotBlank
    private String title;

    @NotBlank
    private String organizerEmail;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}

