package com.travels.bookmybus.mapper;

import com.travels.bookmybus.dto.StopDto;
import com.travels.bookmybus.model.Stop;

public class StopMapper {
    public static Stop toMapEntity(StopDto stopDto){
        Long id;
        if (stopDto.getId() != null) {
            try {
                id= stopDto.getId();
            } catch (NumberFormatException e) {
                // Handle invalid id format
                id=null;
            }
        } else {
            // Set to null for new records
            id=null;
        }
        return Stop.builder()
                .id(id)
                .isFirstStop(stopDto.getIsFirstStop())
                .isLastStop(stopDto.getIsLastStop())
                .stopName(stopDto.getStopName())
                .arrivesAt(stopDto.getArrivesAt())
                .leavesAt(stopDto.getLeavesAt())
                .build();
    }

    public static StopDto toMapDto(Stop stop){
        Long id;
        if (stop.getId() != null) {
            try {
                id= stop.getId();
            } catch (NumberFormatException e) {
                // Handle invalid id format
                id=null;
            }
        } else {
            // Set to null for new records
            id=null;
        }
        return StopDto.builder()
                .id(id)
                .isFirstStop(stop.getIsFirstStop())
                .isLastStop(stop.getIsLastStop())
                .stopName(stop.getStopName())
                .arrivesAt(stop.getArrivesAt())
                .leavesAt(stop.getLeavesAt())
                .build();
    }
}
