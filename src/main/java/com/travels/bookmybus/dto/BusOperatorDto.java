package com.travels.bookmybus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusOperatorDto {
    private Long id;
    // Bus basic details
    @NotBlank(message = "Field should not be blank")
    private String registerNumber;
    @NotBlank(message = "Field should not be blank")
    private String name;
    @NotBlank(message = "Field should not be blank")
    private String mobile;
    @NotBlank(message = "Field should not be blank")
    private String email;
    private String operatorPassword;
    @NotBlank(message = "Field should not be blank")
    private String driverMobile;

    // Bus features
    @NotNull(message = "Field should not be blank")
    private Boolean hasAirConditioner;
    @NotNull(message = "Field should not be blank")
    private Boolean hasWifi;
    @NotNull(message = "Field should not be blank")
    private Boolean hasToiletInBus;

    // Bus Seat configuration and deck information
    @NotNull(message = "Field should not be blank")
    private Boolean hasUpperDeck;
    @NotNull(message = "Field should not be blank")
    private Boolean hasStandardSeater;
    @NotNull(message = "Field should not be blank")
    private Boolean hasSemiSleeper;
    @NotNull(message = "Field should not be blank")
    private Boolean hasSleeper;
    @NotBlank(message = "Field should not be blank")
    @Pattern(regexp = "^(3[0-9]|4[0-9]|5[0-9]|6[0-9]|7[0-3])$", message = "Value must be a number between 30 and 73")
    private String totalSeatsCount;

    @NotBlank(message = "Field should not be blank")
    private String lowerLeftSeaterType;
    @Pattern(regexp = "^[1-2]$", message = "Value must be a 1 or 2")
    private String lowerLeftSeatCountInEachRow;
    @Pattern(regexp = "^(5|6|7|8|9|10|11)$", message = "Value must be between 5 and 11")
    private String lowerLeftTotalRowsCount;

    @NotBlank(message = "Field should not be blank")
    private String lowerRightSeaterType;
    @Pattern(regexp = "^[2-3]$", message = "Value must be 2 or 3")
    private String lowerRightSeatCountInEachRow;
    @Pattern(regexp = "^(5|6|7|8|9|10|11)$", message = "Value must be between 5 and 11")
    private String lowerRightTotalRowsCount;

    @Builder.Default
    private String upperTotalRowsCount="0";

    // Rest stop details
    private Boolean hasRestStop;
    private String restStopAtPlace;
    private LocalTime restStopTime;

    private List<SeatDto> seats = new ArrayList<>(); //1LDL,1LDW,1LSW -> 1UDL,2UDW,2USW -->

    private List<RouteDto> routes = new ArrayList<>();

    private List<StopDto> allBusStops = new ArrayList<>();

    private List<BookingDto> seatBookedStatus = new ArrayList<>();

    // Fare details
//    @NotBlank(message = "Field should not be blank")
    @Builder.Default
    private String baseFare = "950.99";
//    @NotBlank(message = "Field should not be blank")
    private String insuranceFee = "10.00"; // Insurance fee, added optionally per ticket
}
