package com.travels.bookmybus.repository;

import com.travels.bookmybus.model.BusOperator;
import com.travels.bookmybus.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route,Long> {
    List<Route> findByDeparturePlaceAndArrivalPlace(String fromPlace, String toPlace);
}
