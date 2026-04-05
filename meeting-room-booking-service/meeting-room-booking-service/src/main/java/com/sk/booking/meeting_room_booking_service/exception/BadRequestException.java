package com.sk.booking.meeting_room_booking_service.exception;

public class BadRequestException extends RuntimeException {
    public  BadRequestException(String message){
        super(message);
    }
}
