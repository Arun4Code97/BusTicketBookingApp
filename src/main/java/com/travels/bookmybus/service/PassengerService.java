package com.travels.bookmybus.service;

import com.travels.bookmybus.dto.BusOperatorDto;
import com.travels.bookmybus.dto.PassengerDto;
import com.travels.bookmybus.dto.SearchDto;
import com.travels.bookmybus.dto.SeatDto;
import com.travels.bookmybus.mapper.BusMapper;
import com.travels.bookmybus.mapper.PassengerMapper;
import com.travels.bookmybus.model.BusOperator;
import com.travels.bookmybus.model.Passenger;
import com.travels.bookmybus.model.Route;
import com.travels.bookmybus.model.Seat;
import com.travels.bookmybus.repository.BusRepository;
import com.travels.bookmybus.repository.PassengerRepository;
import com.travels.bookmybus.repository.RouteRepository;
import com.travels.bookmybus.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final SeatRepository seatRepository;
    public PassengerDto savePassenger(PassengerDto passengerDto){
        Passenger receivedPassenger = PassengerMapper.toMapEntity(passengerDto);
        return PassengerMapper.toMapDto(passengerRepository.save(receivedPassenger));
    }
    public PassengerDto getById(Long id){
        Optional<Passenger> receivedPassenger = passengerRepository.findById(id);
        return receivedPassenger.map(PassengerMapper::toMapDto)
                .orElseThrow(() -> new NoSuchElementException("Given Passenger Id not found"));
    }
    public List<PassengerDto> getAll(){
        List<Passenger> receivedPassengers = passengerRepository.findAll();
        if(receivedPassengers.isEmpty())
            throw new NoSuchElementException("Passenger details are not added in the database");
        return receivedPassengers.stream().map(PassengerMapper::toMapDto).toList();
    }
    public boolean isExistByEmailId(String email) {
        return passengerRepository.existsByEmail(email);
    }

    public PassengerDto getPassengerByEmailId(String email) {
        Optional<Passenger> receivedPassenger = passengerRepository.findByEmail(email);
        return receivedPassenger.map(PassengerMapper::toMapDto)
                .orElseThrow(() -> new NoSuchElementException("Passenger Email Id not found in the database"));
    }
    public List<BusOperatorDto> getBusesByRoute(SearchDto searchDto, Long passengerId) {
        List<Route> retrievedRoutes = routeRepository.findByDeparturePlaceAndArrivalPlace(
                searchDto.getFromPlace(),
                searchDto.getToPlace() );

        System.out.println("\n\n\n\n Retrieved Routes In Searched Route -Size  :\t"
                + retrievedRoutes.size());
        // Assuming Route has a collection of BusOperators
        List<BusOperator> busOperators = new ArrayList<>();
        for (Route route : retrievedRoutes) {
            System.out.println("retrived routes -> buses" + route.getBusOperator());
            busOperators.add(route.getBusOperator()); // This assumes Route has a getBusOperators() method
        }

        return busOperators.stream()
                .map(BusMapper::toMapDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public boolean bookSeats(List<String> selectedSeatNumbers) {
        try {
            // Loop through the selected seat numbers and mark them as booked
            for (String seatId : selectedSeatNumbers) {
                seatId= seatId.trim();

                Seat seat = new Seat();
                Long id = Long.parseLong(seatId);
                if (!seatId.isEmpty()) {
                seat= seatRepository.findById(id).get();
                }

                // If the seat is found and is not already booked, mark it as booked
                if (!seat.getIsBooked()) {
                    seat.setIsBooked(true);
                    seatRepository.save(seat); // Save the updated seat status
                } else {
                    // Log if the seat is already booked
                    System.out.println("Seat " + seatId + " is already booked or not found.");
                }

            }
            return true; // Successfully booked seats
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Booking failed due to an error
        }
    }


    public LocalTime extractTimeFromPoint(String point) {
        String timeRegex = "\\((.*?)\\)"; // Regex to extract the time inside parentheses
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(point);

        if (matcher.find()) {
            String timeText = matcher.group(1); // Extracted time string, e.g., "9:30 PM"
            try {
                // Define a formatter for the 12-hour format with AM/PM
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                return LocalTime.parse(timeText, formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        }
        return null; // Return null if the time cannot be parsed
    }


}
