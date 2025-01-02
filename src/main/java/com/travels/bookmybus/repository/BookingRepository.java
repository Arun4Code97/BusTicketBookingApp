package com.travels.bookmybus.repository;

import com.travels.bookmybus.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByPassengerIdOrderByPassengerBoardingDateDesc(Long passengerId);
}
