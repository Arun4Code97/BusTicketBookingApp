package com.travels.bookmybus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDto {
    @NotBlank(message = "Field should not be blank")
    private String fromPlace; //Starting destination or From
    @NotBlank(message = "Field should not be blank")
    private String toPlace;  // Ending destination or TO
    @NotNull(message = "field should not be null")
    private LocalDate tripDate;
}
