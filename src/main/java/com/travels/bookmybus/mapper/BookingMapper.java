package com.travels.bookmybus.mapper;

import com.travels.bookmybus.dto.BookingDto;
import com.travels.bookmybus.model.Booking;

public class BookingMapper {
    public static Booking toMapEntity(BookingDto bookingDto) {
        Long id;
        if (bookingDto.getId() != null) {
            try {
                id = bookingDto.getId();
            } catch (NumberFormatException e) {
                // Handle invalid id format
                id = null;
            }
        } else {
            // Set to null for new records
            id = null;
        }
        return Booking.builder()
                .id(id)
                .seatNumber(bookingDto.getSeatNumber())
                .bookedDate(bookingDto.getBookedDate())
                .bookedTime(bookingDto.getBookedTime())
                .journeyDate(bookingDto.getJourneyDate())
                .journeyTime(bookingDto.getJourneyTime())
                .passenger(bookingDto.getPassenger())
                .build();
    }
    public static BookingDto toMapDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .seatNumber(booking.getSeatNumber())
                .bookedDate(booking.getBookedDate())
                .bookedTime(booking.getBookedTime())
                .journeyDate(booking.getJourneyDate())
                .journeyTime(booking.getJourneyTime())
                .passenger(booking.getPassenger())
                .build();
    }
}
