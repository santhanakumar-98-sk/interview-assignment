package com.sk.booking.meeting_room_booking_service.exception;

public class ConflictException extends RuntimeException {
        public  ConflictException(String message){
            super(message);
        }
}
