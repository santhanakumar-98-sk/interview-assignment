package com.sk.booking.meeting_room_booking_service.entity;

import com.sk.booking.meeting_room_booking_service.util.IdempotencyStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "idempotency_records",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"idempotencyKey", "organizerEmail"}
        ))
@Data
public class IdempotencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idempotencyKey;
    private String organizerEmail;

    private Long bookingId;

    @Enumerated(EnumType.STRING)
    private IdempotencyStatus status;
}
