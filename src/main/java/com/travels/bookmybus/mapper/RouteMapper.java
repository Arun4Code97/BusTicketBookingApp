package com.travels.bookmybus.mapper;

import com.travels.bookmybus.dto.RouteDto;
import com.travels.bookmybus.model.Route;

public class RouteMapper {
    public static Route toMapEntity(RouteDto routeDto){
        Long id;
        if (routeDto.getId() != null) {
            try {
                id= routeDto.getId();
            } catch (NumberFormatException e) {
                // Handle invalid id format
                id=null;
            }
        } else {
            // Set to null for new records
            id=null;
        }
        return Route.builder()
                .id(id)
                .departurePlace(routeDto.getDeparturePlace())
                .arrivalPlace(routeDto.getArrivalPlace())
                .departureTime(routeDto.getDepartureTime())
                .arrivalTime(routeDto.getArrivalTime())
                .isEvenDay(routeDto.getIsEvenDay())
                .build();
    }

    public static RouteDto toMapDto(Route route){
        Long id;
        if (route.getId() != null) {
            try {
                id= route.getId();
            } catch (NumberFormatException e) {
                // Handle invalid id format
                id=null;
            }
        } else {
            // Set to null for new records
            id=null;
        }
        return RouteDto.builder()
                .id(id)
                .departurePlace(route.getDeparturePlace())
                .arrivalPlace(route.getArrivalPlace())
                .departureTime(route.getDepartureTime())
                .arrivalTime(route.getArrivalTime())
                .isEvenDay(route.getIsEvenDay())
                .build();
    }
}
