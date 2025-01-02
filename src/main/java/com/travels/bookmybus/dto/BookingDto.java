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
    private String seatType;
    private Double seatFare;

    private String passengerName;
    private int age;
    private String gender;
    private Long contactNumber;
    private Long secondaryNumber;
    private String email;

    private LocalDate bookedDate;
    private LocalTime bookedTime;

    private String passengerBoardingPlace;
    private LocalDate passengerBoardingDate;
    private LocalTime passengerBoardingTime;

    private String passengerDroppingPlace;
    private LocalDate passengerDroppingDate;
    private LocalTime passengerDroppingTime;

    private String busDepartureLocation;
    private String busArrivalLocation;

    private Long passengerId;
    private Long busId;
    private Long seatId;
}
