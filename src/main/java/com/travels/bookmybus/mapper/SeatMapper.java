package com.travels.bookmybus.mapper;

import com.travels.bookmybus.dto.SeatDto;
import com.travels.bookmybus.model.Seat;

public class SeatMapper {
    public static Seat toMapEntity(SeatDto seatDto){
        Long id;
        if (seatDto.getId() != null) {
            try {
                id= seatDto.getId();
            } catch (NumberFormatException e) {
                // Handle invalid id format
                id=null;
            }
        } else {
            // Set to null for new records
            id=null;
        }
        return Seat.builder()
                .id(id)
                .seatNumber(seatDto.getSeatNumber())
                .seatType(seatDto.getSeatType())
                .isBooked(seatDto.getIsBooked())
                .build();
    }
    public static SeatDto toMapDto(Seat seat){
        return SeatDto.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .seatType(seat.getSeatType())
                .isBooked(seat.getIsBooked())
                .build();
    }
}
