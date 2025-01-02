package com.travels.bookmybus.repository;

import com.travels.bookmybus.model.Seat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface SeatRepository extends JpaRepository<Seat,Long> {
    List<Seat> findByBusOperatorIdAndTripDate(Long busOperatorId, LocalDate tripDate);
}
