package com.travels.bookmybus.service;

import com.travels.bookmybus.dto.BookingDto;
import com.travels.bookmybus.mapper.BookingMapper;
import com.travels.bookmybus.model.Booking;
import com.travels.bookmybus.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    public BookingDto saveBooking(BookingDto bookingDto){
        Booking receivedBooking = BookingMapper.toMapEntity(bookingDto);
        return BookingMapper.toMapDto(bookingRepository.save(receivedBooking));
    }
    public BookingDto getById(Long id){
        Optional<Booking> receivedBooking = bookingRepository.findById(id);
        return receivedBooking.map(BookingMapper::toMapDto)
                .orElseThrow(() -> new NoSuchElementException("Given Booking Id not found"));
    }
    public List<BookingDto> getAll(){
        List<Booking> receivedBookings = bookingRepository.findAll();
        if(receivedBookings.isEmpty())
            throw new NoSuchElementException("Bookings details are not added in the database");
        return receivedBookings.stream().map(BookingMapper::toMapDto).toList();
    }
}
