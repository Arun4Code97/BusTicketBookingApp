package com.travels.bookmybus.service;

import com.travels.bookmybus.dto.RouteDto;
import com.travels.bookmybus.mapper.RouteMapper;
import com.travels.bookmybus.model.Route;
import com.travels.bookmybus.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    public RouteDto saveRoute(RouteDto routeDto){
        Route receivedRoute = RouteMapper.toMapEntity(routeDto);
        return RouteMapper.toMapDto(routeRepository.save(receivedRoute));
    }
    public RouteDto getById(Long id){
        Optional<Route> receivedRoute = routeRepository.findById(id);
        return receivedRoute.map(RouteMapper::toMapDto)
                .orElseThrow(() -> new NoSuchElementException("Given route Id not found"));
    }
    public List<RouteDto> getAll(){
        List<Route> receivedRoutes = routeRepository.findAll();
        if(receivedRoutes.isEmpty())
            throw new NoSuchElementException("route details are not added in the database");
        return receivedRoutes.stream().map(RouteMapper::toMapDto).toList();
    }
}
