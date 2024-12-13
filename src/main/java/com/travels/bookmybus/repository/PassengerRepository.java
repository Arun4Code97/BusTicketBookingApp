package com.travels.bookmybus.repository;

import com.travels.bookmybus.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger,Long> {
    Optional<Passenger> findByEmail(String email);

    boolean existsByEmail(String email);
}
