package com.sk.booking.meeting_room_booking_service.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message){
        super(message);
    }
}

