package com.travels.bookmybus.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupedBookingDto {
    private LocalDate bookingDate;
    private String route;
    private String busOperatorName;
    private String busNumber;
    private String driverContact;
    private String boardingPoint;
    private LocalDate boardingDate;
    private String boardingTime;
    private String droppingPoint;
    private LocalDate droppingDate;
    private String droppingTime;
    private List<BookingDto> bookings;
    private double totalFare;
}
