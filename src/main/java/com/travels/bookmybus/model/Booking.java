package com.travels.bookmybus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Booking {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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
    @ManyToOne
    @JoinColumn(name = "bus_operator_id")
    @ToString.Exclude
    @JsonBackReference
    private BusOperator busOperator;


}
