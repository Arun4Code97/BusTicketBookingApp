package com.travels.bookmybus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean isFirstStop;
    private Boolean isLastStop;
    private String stopName;
    private LocalTime arrivesAt;
    private LocalTime leavesAt;
    @ManyToOne
    @JoinColumn(name = "busOperator_id")

    @ToString.Exclude
    @JsonBackReference
    private BusOperator busOperator;
}
