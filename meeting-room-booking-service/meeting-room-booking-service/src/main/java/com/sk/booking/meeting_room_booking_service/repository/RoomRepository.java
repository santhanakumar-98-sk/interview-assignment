package com.sk.booking.meeting_room_booking_service.repository;

import com.sk.booking.meeting_room_booking_service.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {

    @Query("""
            SELECT DISTINCT r FROM Room r JOIN FETCH r.amenities
            WHERE (:minCapacity IS NULL OR r.capacity >= :minCapacity)
            AND (:amenityName IS NULL OR :amenityName MEMBER OF r.amenities)
            """
            )
    List<Room> searchRooms(Integer minCapacity, String amenityName);

    Optional<Room> findByNameIgnoreCase(String name);

    Optional<Room> findByName(String roomName);
}
