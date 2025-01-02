package com.travels.bookmybus.repository;

import com.travels.bookmybus.model.BusOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<BusOperator,Long> {
    Optional<BusOperator> findByEmail(String email);
    boolean existsByEmail(String email);
}
