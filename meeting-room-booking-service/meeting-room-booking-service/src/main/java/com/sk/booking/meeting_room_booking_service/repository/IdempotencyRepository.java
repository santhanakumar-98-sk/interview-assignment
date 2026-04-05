package com.sk.booking.meeting_room_booking_service.repository;

import com.sk.booking.meeting_room_booking_service.entity.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IdempotencyRepository extends JpaRepository<IdempotencyRecord,Long> {

    Optional<IdempotencyRecord> findByIdempotencyKeyAndOrganizerEmail(String key, String email);

}
