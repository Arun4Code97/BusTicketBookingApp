package com.travels.bookmybus.service;

import com.travels.bookmybus.dto.*;
import com.travels.bookmybus.mapper.BusMapper;
import com.travels.bookmybus.mapper.RouteMapper;
import com.travels.bookmybus.model.BusOperator;
import com.travels.bookmybus.model.Route;
import com.travels.bookmybus.model.Seat;
import com.travels.bookmybus.model.Stop;
import com.travels.bookmybus.repository.BusRepository;
import com.travels.bookmybus.repository.RouteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class BusOperatorService {
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    public BusOperatorDto saveBus(BusOperatorDto busOperatorDto){
        BusOperator receivedBusOperator = BusMapper.toMapEntity(busOperatorDto);
        return BusMapper.toMapDto(busRepository.save(receivedBusOperator));
    }
    public BusOperatorDto getById(Long id){
        Optional<BusOperator> receivedBus = busRepository.findById(id);
        return receivedBus.map(BusMapper::toMapDto)
                .orElseThrow(() -> new NoSuchElementException("Given Bus Id not found"));
    }
    public List<BusOperatorDto> getAll(){
        List<BusOperator> receivedBusOperators = busRepository.findAll();
        if(receivedBusOperators.isEmpty())
            throw new NoSuchElementException("No bus details are not added in the database");
        return receivedBusOperators.stream().map(BusMapper::toMapDto).toList();
    }

    public boolean isExistByEmailId(String email) {
        return busRepository.existsByEmail(email);
    }
    public BusOperatorDto getOperatorByEmailId(String email) {
        Optional<BusOperator> receivedOperator = busRepository.findByEmail(email);
        return receivedOperator.map(BusMapper::toMapDto)
                .orElseThrow(() -> new NoSuchElementException("Bus Operator Email Id not found in the database"));
    }

    public void saveSeatConfiguration(Long busOperatorId, List<String> seatNumbers) {
        // Fetch the BusOperator
        BusOperator busOperator = busRepository.findById(busOperatorId)
                .orElseThrow(() -> new EntityNotFoundException("Bus Operator not found"));


        String lowerLeftSeaterType = busOperator.getLowerLeftSeaterType();
        String lowerRightSeaterType = busOperator.getLowerRightSeaterType();

        // Map seat numbers to SeatDto
        List<Seat> seats = seatNumbers.stream()
                .map(seatNumber -> {
                    String seatType = determineSeatType(seatNumber, lowerLeftSeaterType, lowerRightSeaterType);

                    return Seat.builder()
                            .seatNumber(seatNumber)
                            .seatType(seatType)
                            .isBooked(false) // Default to not booked
                            .busOperator(busOperator)
                            .build();
                                    }
                    )
                .toList();

        // Save seats to BusOperator
        busOperator.getSeats().clear(); // Clear any existing seat data
        busOperator.getSeats().addAll(seats);

        // Save to repository
        busRepository.save(busOperator);
    }
    private String determineSeatType(String seatNumber, String lowerLeftSeaterType, String lowerRightSeaterType) {
        // If the seat number starts with 'U', it's an upper deck seat, so set it to "sleeper"
        if (seatNumber.startsWith("U")) {
            return "sleeper"; // Upper deck seats are sleepers by default
        }

        if (seatNumber.startsWith("L")) {
            // Check if it's the left side (L1L1, L2L1) or right side (L1R1, L2R1, etc.)
            if (seatNumber.contains("R")) {
                return lowerRightSeaterType; // Left side of lower deck
            } else
                return lowerLeftSeaterType; // Right side of lower deck

        }
        return "unknown";
    }


    public void saveRouteConfiguration(Long busOperatorId, RouteDto routeDto) {
        // Fetch the BusOperator
        BusOperator busOperator = busRepository.findById(busOperatorId)
                .orElseThrow(() -> new EntityNotFoundException("Bus Operator not found"));

        Route route = RouteMapper.toMapEntity(routeDto);
        route.setBusOperator(busOperator);

        List<Route> incomingRoute = List.of(route);

        // Save seats to BusOperator
        busOperator.getRoutes().clear();// Clear any existing seat data
        busOperator.getRoutes().addAll(incomingRoute);

        busRepository.save(busOperator);
    }

    public void saveStopConfiguration(Long busOperatorId, List<StopDto> stopDtos) {
        // Fetch the BusOperator
        BusOperator busOperator = busRepository.findById(busOperatorId)
                .orElseThrow(() -> new EntityNotFoundException("Bus Operator not found"));

        // Set the isFirstStop and isLastStop based on the position in the list
        if (!stopDtos.isEmpty()) {
            // Set the first stop
            stopDtos.get(0).setIsFirstStop(true);
            // Set the last stop
            stopDtos.get(stopDtos.size() - 1).setIsLastStop(true);
        }

        // Map the StopDtos to Stop entities
        List<Stop> stops = stopDtos.stream()
                .map(dto -> Stop.builder()
                        .stopName(dto.getStopName())
                        .arrivesAt(dto.getArrivesAt())
                        .leavesAt(dto.getLeavesAt())
                        .isFirstStop(dto.getIsFirstStop())
                        .isLastStop(dto.getIsLastStop())
                        .busOperator(busOperator)  // Ensure busOperator is set for each stop
                        .build())
                .toList();  // Collect to List

        // Make sure collection is initialized and replace it completely
        busOperator.getAllBusStops().clear();  // Clear the existing stops

        // Add all the new stops back to the collection
        busOperator.getAllBusStops().addAll(stops);  // Add new stops

        // Save the BusOperator (This will also save the associated stops due to cascade)
        busRepository.save(busOperator);
    }

}




