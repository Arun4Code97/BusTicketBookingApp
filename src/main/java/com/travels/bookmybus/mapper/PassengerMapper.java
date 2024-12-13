package com.travels.bookmybus.mapper;

import com.travels.bookmybus.dto.PassengerDto;
import com.travels.bookmybus.model.Passenger;

public class PassengerMapper {
    public static Passenger toMapEntity(PassengerDto passengerDto) {
        Long id;
        if (passengerDto.getId() != null) {
            try {
                id = passengerDto.getId();
            } catch (NumberFormatException e) {
                // Handle invalid id format
                id = null;
            }
        } else {
            // Set to null for new records
            id = null;
        }
        return Passenger.builder()
                .id(id)
                .firstName(passengerDto.getFirstName())
                .lastName(passengerDto.getLastName())
                .age( Integer.parseInt(passengerDto.getAge()) )
                .gender(passengerDto.getGender())
                .email(passengerDto.getEmail())
                .primaryMobile( Long.parseLong(passengerDto.getPrimaryMobile()) )
                .secondaryMobile( Long.parseLong(passengerDto.getSecondaryMobile()) )
                .city(passengerDto.getCity())
                .address(passengerDto.getAddress())
                .password(passengerDto.getPassword())
                .build();
    }

    public static PassengerDto toMapDto(Passenger passenger) {

        return PassengerDto.builder()
                .id(passenger.getId())
                .firstName(passenger.getFirstName())
                .lastName(passenger.getLastName())
                .age(passenger.getAge().toString())
                .gender(passenger.getGender())
                .email(passenger.getEmail())
                .primaryMobile(passenger.getPrimaryMobile().toString())
                .secondaryMobile(passenger.getSecondaryMobile().toString())
                .city(passenger.getCity())
                .address(passenger.getAddress())
                .password(passenger.getPassword())
                .build();
    }
}
