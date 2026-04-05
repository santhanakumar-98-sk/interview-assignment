package com.sk.booking.meeting_room_booking_service.repository;

import com.sk.booking.meeting_room_booking_service.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
    SELECT CASE WHEN EXISTS (
        SELECT 1 FROM Booking b
        WHERE b.room.id = :roomId
        AND b.status ='CONFIRMED'
        AND b.startTime < :endTime
        AND b.endTime > :startTime
    ) THEN true ELSE false END
""")
    boolean existsConflictingBooking(
            Long roomId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    @Query("""
    SELECT b FROM Booking b
    WHERE (:roomId IS NULL OR b.room.id = :roomId)
    AND (:from IS NULL OR b.endTime >= :from)
    AND (:to IS NULL OR b.startTime <= :to)
    """)
    Page<Booking> searchBookings(
            Long roomId,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );

    @Query("""
    SELECT b FROM Booking b
    WHERE b.status = 'CONFIRMED'
    AND b.startTime < :to
    AND b.endTime > :from
    """)
    List<Booking> findBookingsInRange(
            LocalDateTime from,
            LocalDateTime to
    );

}

