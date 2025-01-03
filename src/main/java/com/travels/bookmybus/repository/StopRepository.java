package com.travels.bookmybus.repository;

import com.travels.bookmybus.model.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StopRepository extends JpaRepository<Stop,Long> {
    List<Stop> findByBusOperatorId(Long busId);
}
