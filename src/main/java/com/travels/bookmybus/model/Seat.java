package com.travels.bookmybus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  seatNumber;
    private String seatType;
    private LocalDate tripDate;
    private Boolean isBooked;
    @ManyToOne
    @JoinColumn(name = "bus_operator_id")
    @ToString.Exclude
    @JsonBackReference
    private BusOperator busOperator;
}
