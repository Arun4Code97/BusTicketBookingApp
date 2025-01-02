package com.travels.bookmybus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stops {
    List<StopDto> boardingPoints ;
    List<StopDto> droppingPoints;
}
