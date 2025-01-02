package com.travels.bookmybus.mapper;

import com.travels.bookmybus.dto.BookingDto;
import com.travels.bookmybus.model.Booking;
import com.travels.bookmybus.model.BusOperator;

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
                .seatType(bookingDto.getSeatType())
                .seatFare(bookingDto.getSeatFare())

                .passengerName(bookingDto.getPassengerName())
                .age(bookingDto.getAge())
                .gender(bookingDto.getGender())
                .contactNumber(bookingDto.getContactNumber())
                .secondaryNumber(bookingDto.getSecondaryNumber())
                .email(bookingDto.getEmail())

                .bookedDate(bookingDto.getBookedDate())
                .bookedTime(bookingDto.getBookedTime())

                .passengerBoardingPlace(bookingDto.getPassengerBoardingPlace())
                .passengerBoardingDate(bookingDto.getPassengerBoardingDate())
                .passengerBoardingTime(bookingDto.getPassengerBoardingTime())

                .passengerDroppingPlace(bookingDto.getPassengerDroppingPlace())
                .passengerDroppingDate(bookingDto.getPassengerDroppingDate())
                .passengerDroppingTime(bookingDto.getPassengerDroppingTime())

                .busDepartureLocation(bookingDto.getBusDepartureLocation())
                .busArrivalLocation(bookingDto.getBusArrivalLocation())

                .busId(bookingDto.getBusId())
                .passengerId(bookingDto.getPassengerId())
                .seatId(bookingDto.getSeatId())

                .build();
    }
    public static BookingDto toMapDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())

                .seatNumber(booking.getSeatNumber())
                .seatType(booking.getSeatType())
                .seatFare(booking.getSeatFare())

                .passengerName(booking.getPassengerName())
                .age(booking.getAge())
                .gender(booking.getGender())
                .contactNumber(booking.getContactNumber())
                .secondaryNumber(booking.getSecondaryNumber())
                .email(booking.getEmail())

                .bookedDate(booking.getBookedDate())
                .bookedTime(booking.getBookedTime())

                .passengerBoardingPlace(booking.getPassengerBoardingPlace())
                .passengerBoardingDate(booking.getPassengerBoardingDate())
                .passengerBoardingTime(booking.getPassengerBoardingTime())

                .passengerDroppingPlace(booking.getPassengerDroppingPlace())
                .passengerDroppingDate(booking.getPassengerDroppingDate())
                .passengerDroppingTime(booking.getPassengerDroppingTime())

                .busDepartureLocation(booking.getBusDepartureLocation())
                .busArrivalLocation(booking.getBusArrivalLocation())


                .busId(booking.getBusId())
                .passengerId(booking.getPassengerId())
                .seatId(booking.getSeatId())

                .build();
    }
}
