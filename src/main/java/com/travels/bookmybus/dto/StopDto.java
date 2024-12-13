package com.travels.bookmybus.dto;

import com.travels.bookmybus.model.BusOperator;
import lombok.*;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StopDto {
    private Long id;
    private Boolean isFirstStop;
    //    private Boolean isRestStop;
    private Boolean isLastStop;
    private String stopName;
    private LocalTime arrivesAt;
    private LocalTime leavesAt;

}
