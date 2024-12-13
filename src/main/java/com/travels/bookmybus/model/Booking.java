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
public class Booking {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String seatNumber;
    private LocalDate bookedDate;
    private LocalTime bookedTime;
    private LocalDate journeyDate;
    private LocalTime journeyTime;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "bus_operator_id")
    @ToString.Exclude
    @JsonBackReference
    private BusOperator busOperator;


}
