package com.travels.bookmybus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
//@ToString
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String departurePlace;
    private String arrivalPlace;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    // Alternate day scheduling
    private Boolean isEvenDay;  // true for even days, false for odd days
    @ManyToOne
    @JoinColumn(name = "bus_operator_id")
    @ToString.Exclude
    @JsonBackReference
    private BusOperator busOperator;
}
