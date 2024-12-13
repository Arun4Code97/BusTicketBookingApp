package com.travels.bookmybus.dto;

import com.travels.bookmybus.model.BusOperator;
import com.travels.bookmybus.model.Passenger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    private String seatNumber;
    private LocalDate bookedDate;
    private LocalTime bookedTime;
    private LocalDate journeyDate;
    private LocalTime journeyTime;

    private Passenger passenger;
}
