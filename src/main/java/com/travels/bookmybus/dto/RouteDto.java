package com.travels.bookmybus.dto;

import com.travels.bookmybus.model.BusOperator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteDto {

    private Long id;

    @NotBlank(message = "Field should not be blank")
    private String departurePlace; //Starting destination or From
    @NotBlank(message = "Field should not be blank")
    private String arrivalPlace;  // Ending destination or TO
    @NotNull(message = "Departure time is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime departureTime;
    @NotNull(message = "Arrival time is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime arrivalTime;

    // Alternate day scheduling
    @NotNull(message = "isEvenDay is required")
    private Boolean isEvenDay;  // true for even days, false for odd days
}
