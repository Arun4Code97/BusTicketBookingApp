package com.travels.bookmybus.dto;

import java.util.List;

public class SeatBookingRequestDto {
    private List<Long> seats;

    // Getters and setters
    public List<Long> getSeats() {
        return seats;
    }

    public void setSeats(List<Long> seats) {
        this.seats = seats;
    }
}
