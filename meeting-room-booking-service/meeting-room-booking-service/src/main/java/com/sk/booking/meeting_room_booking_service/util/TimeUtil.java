package com.sk.booking.meeting_room_booking_service.util;

import java.time.*;

public final class TimeUtil {

    public static double calculateOverlapHours(
            LocalDateTime bookingStart,
            LocalDateTime bookingEnd,
            LocalDateTime from,
            LocalDateTime to) {

        LocalDateTime start = bookingStart.isAfter(from) ? bookingStart : from;
        LocalDateTime end = bookingEnd.isBefore(to) ? bookingEnd : to;

        if (!start.isBefore(end)) {
            return 0;
        }

        return Duration.between(start, end).toMinutes() / 60.0;
    }

    public static double calculateBusinessHours(
            LocalDateTime from,
            LocalDateTime to) {

        double total = 0;
        LocalDate date = from.toLocalDate();

        while (!date.isAfter(to.toLocalDate())) {

            DayOfWeek day = date.getDayOfWeek();

            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {

                LocalDateTime dayStart = date.atTime(8, 0);
                LocalDateTime dayEnd = date.atTime(20, 0);

                LocalDateTime start = from.isAfter(dayStart) ? from : dayStart;
                LocalDateTime end = to.isBefore(dayEnd) ? to : dayEnd;

                if (start.isBefore(end)) {
                    total += Duration.between(start, end).toMinutes() / 60.0;
                }
            }

            date = date.plusDays(1);
        }

        return total;
    }
}
