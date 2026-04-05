package com.sk.booking.meeting_room_booking_service.service;

import com.sk.booking.meeting_room_booking_service.dto.BookingListResponse;
import com.sk.booking.meeting_room_booking_service.dto.BookingRequest;
import com.sk.booking.meeting_room_booking_service.dto.BookingResponse;
import com.sk.booking.meeting_room_booking_service.entity.Booking;
import com.sk.booking.meeting_room_booking_service.entity.IdempotencyRecord;
import com.sk.booking.meeting_room_booking_service.entity.Room;
import com.sk.booking.meeting_room_booking_service.exception.ConflictException;
import com.sk.booking.meeting_room_booking_service.exception.NotFoundException;
import com.sk.booking.meeting_room_booking_service.repository.BookingRepository;
import com.sk.booking.meeting_room_booking_service.repository.IdempotencyRepository;
import com.sk.booking.meeting_room_booking_service.util.BookingValidator;
import com.sk.booking.meeting_room_booking_service.util.BookingStatus;
import com.sk.booking.meeting_room_booking_service.util.IdempotencyStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingValidator bookingValidator;
    private final IdempotencyRepository idempotencyRepository;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {

        bookingValidator.validateBookingRequest(request);
        Room room  = bookingValidator.resolveRoom(request);

        boolean exists  = bookingRepository.existsConflictingBooking(
                room.getId(),
                LocalDateTime.parse(request.getStartTime()),
                LocalDateTime.parse(request.getEndTime())
        );

        if (exists) {
            throw new ConflictException("Room already booked for this time");
        }

        Booking booking = Booking.builder()
                .room(room)
                .title(request.getTitle())
                .organizerEmail(request.getOrganizerEmail())
                .startTime(LocalDateTime.parse(request.getStartTime()))
                .endTime(LocalDateTime.parse(request.getEndTime()))
                .status(BookingStatus.CONFIRMED)
                .build();

        booking = bookingRepository.save(booking);

        return map(booking);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if(booking.getStatus() == BookingStatus.CANCELLED){
            return  map(booking);
        }

        bookingValidator.cancellation(booking);

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        return map(booking);
    }

    @Override
    public BookingListResponse getBookings(Long roomId, LocalDateTime from,
                                           LocalDateTime to, int limit, int offset) {

        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

        Page<Booking> bookingPage =
                bookingRepository.searchBookings(roomId, from, to, pageable);

        List<BookingResponse>  bookings = bookingPage.getContent()
                .stream()
                .map(this::map)
                .toList();

        return BookingListResponse.builder()
                .bookings(bookings)
                .total(bookingPage.getTotalElements())
                .limit(limit)
                .offset(offset)
                .build();

    }

    private BookingResponse map(Booking b) {
        return BookingResponse.builder()
                .id(b.getId())
                .roomId(b.getRoom().getId())
                .roomName(b.getRoom().getName())
                .title(b.getTitle())
                .organizerEmail(b.getOrganizerEmail())
                .startTime(b.getStartTime())
                .status(b.getStatus())
                .endTime(b.getEndTime())
                .build();
    }

    @Override
    @Transactional
    public BookingResponse createBookingWithIdempotency(
            String key,
            BookingRequest request) {

        String email = request.getOrganizerEmail();

        Optional<IdempotencyRecord> existing =
                idempotencyRepository
                        .findByIdempotencyKeyAndOrganizerEmail(key, email);

        if (existing.isPresent()) {
            IdempotencyRecord record = existing.get();

            if (record.getStatus() == IdempotencyStatus.COMPLETED) {

                Booking booking = bookingRepository
                        .findById(record.getBookingId())
                        .orElseThrow();

                return map(booking);
            }

            if (record.getStatus() == IdempotencyStatus.PROCESSING) {
                throw new ConflictException("Request already in progress");
            }
        }

        IdempotencyRecord record = new IdempotencyRecord();
        record.setIdempotencyKey(key);
        record.setOrganizerEmail(email);
        record.setStatus(IdempotencyStatus.PROCESSING);

        record = idempotencyRepository.save(record);

        BookingResponse response = createBooking(request);

        record.setStatus(IdempotencyStatus.COMPLETED);
        record.setBookingId(response.getId());
        idempotencyRepository.save(record);

        return response;
    }
}
