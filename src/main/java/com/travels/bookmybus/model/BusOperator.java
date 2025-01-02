package com.travels.bookmybus.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
public class BusOperator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Bus basic details
    private String registerNumber;
    private String name;
    private Long mobile;
    private String email;
    private String password;
    private Long driverMobile;

    // Bus features
    private Boolean hasAirConditioner;
    private Boolean hasWifi;
    private Boolean hasToiletInBus;

    // Bus Seat configuration and deck information
    private Boolean hasUpperDeck;
    private Boolean hasStandardSeater;
    private Boolean hasSemiSleeper;
    private Boolean hasSleeper;
    private Long totalSeatsCount;


    private String lowerLeftSeaterType;
    private Long lowerLeftSeatCountInEachRow;
    private Long lowerLeftTotalRowsCount;

    private String lowerRightSeaterType;
    private Long lowerRightSeatCountInEachRow;
    private Long lowerRightTotalRowsCount;

    private Long upperTotalRowsCount;


    // Rest stop details
    private Boolean hasRestStop;
    private String restStopAtPlace;
    private LocalTime restStopTime;

    @OneToMany(mappedBy = "busOperator", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Seat> seats; //1LDL,1LDW,1LSW -> 1UDL,2UDW,2USW -->

    // Route details with alternate day configuration
    @OneToMany(mappedBy = "busOperator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Route> routes;

    @OneToMany(mappedBy = "busOperator", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Stop> allBusStops = new ArrayList<>();

    @OneToMany(mappedBy = "busOperator")
    @JsonManagedReference
    private List<Booking> seatBookedStatus;

    // Fare details
    private Double baseFareSeaterType;
    private Double baseFareSemiSleeperType;
    private Double baseFareSleeperType;
    private Double insuranceFee;
    // Insurance fee, added optionally per ticket

}
