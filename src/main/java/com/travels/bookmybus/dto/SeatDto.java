package com.travels.bookmybus.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travels.bookmybus.model.BusOperator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatDto {
    private Long id;
    private String  seatNumber;
    private String seatType;
    private LocalDate tripDate;
    private Boolean isBooked;
}
