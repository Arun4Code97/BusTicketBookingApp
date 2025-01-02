package com.travels.bookmybus.service;

import com.travels.bookmybus.dto.SeatDto;
import com.travels.bookmybus.mapper.SeatMapper;
import com.travels.bookmybus.model.Seat;
import com.travels.bookmybus.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    public SeatDto saveSeatDetails(SeatDto seatDto){
        Seat receivedSeat = SeatMapper.toMapEntity(seatDto);
        return SeatMapper.toMapDto(seatRepository.save(receivedSeat));
    }
    public SeatDto getById(Long id){
       Optional<Seat> retrievedSeat = seatRepository.findById(id);
      return retrievedSeat.map(SeatMapper::toMapDto)
              .orElseThrow(() -> new NoSuchElementException("Given Seat Id not found"));
    }
    public Seat getSeatEntityById(Long id){
        Optional<Seat> retrievedSeat = seatRepository.findById(id);
        return retrievedSeat.get();
    }
    public List<SeatDto> getSeatsByBusOperatorAndTripDate(Long busOperatorId, LocalDate tripDate) {
        List<Seat> seats = seatRepository.findByBusOperatorIdAndTripDate(busOperatorId,tripDate);
        return seats.stream().map(SeatMapper::toMapDto).toList();
    }
//    public List<Seat> getSeatsEntityByBusOperator(Long busOperatorId) {
//        return  seatRepository.findByBusOperatorId(busOperatorId);
//    }
    public List<SeatDto> getAll(){
       List<Seat> retrievedSeats = seatRepository.findAll();
       if(retrievedSeats.isEmpty())
           throw new NoSuchElementException("Seat details are not added in the database");
        return retrievedSeats.stream().map(SeatMapper::toMapDto).toList();
    }



    public List<Seat> bookSeats(Long busId, List<Long> seatIds) {
        // Fetch the seats by IDs
        List<Seat> seats = seatRepository.findAllById(seatIds);
        LocalDate tripDate = null;
        if(!seats.isEmpty())
            tripDate = seats.get(0).getTripDate(); //collecting tripDate
        // Check availability and book the seats
        for (Seat seat : seats) {
            if (seat.getIsBooked()) {
                throw new IllegalStateException("Seat " + seat.getSeatNumber() + " is already booked");
            }
            seat.setIsBooked(true);
        }

        // Save the updated seat status
        seatRepository.saveAll(seats);

        // Fetch all seats for the bus to update the UI
        return seatRepository.findByBusOperatorIdAndTripDate(busId,tripDate);
    }
}
