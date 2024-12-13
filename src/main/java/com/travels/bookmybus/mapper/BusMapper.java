package com.travels.bookmybus.mapper;

import com.travels.bookmybus.dto.BusOperatorDto;
import com.travels.bookmybus.model.BusOperator;

public class BusMapper {
    public static BusOperator toMapEntity(BusOperatorDto busOperatorDto) {
        Long id;
        if (busOperatorDto.getId() != null) {
            try {
                id = busOperatorDto.getId();
            } catch (NumberFormatException e) {
                // Handle invalid id format
                id = null;
            }
        } else {
            // Set to null for new records
            id = null;
        }
        String upperRowsCount ="0";

        if(busOperatorDto.getUpperTotalRowsCount() != null) {
            if (!busOperatorDto.getUpperTotalRowsCount().isEmpty()) {
                try {
                    upperRowsCount = busOperatorDto.getUpperTotalRowsCount();
                } catch (NumberFormatException e) {
                    // Handle invalid id format
                    upperRowsCount = "0";
                }
            }
        }  else {
            // Set to zero for new records
            upperRowsCount = "0";
        }
//        String upperRowsCount = Optional.ofNullable(busOperatorDto.getUpperTotalRowsCount())
//                .filter(value -> !value.isEmpty())
//                .orElse("0");

        return BusOperator.builder()
                .id(id)
                .registerNumber(busOperatorDto.getRegisterNumber())
                .name(busOperatorDto.getName())
                .mobile(Long.valueOf(busOperatorDto.getMobile()))
                .email(busOperatorDto.getEmail())
                .password(busOperatorDto.getOperatorPassword())
                .driverMobile(Long.valueOf(busOperatorDto.getDriverMobile()))

                .hasAirConditioner(busOperatorDto.getHasAirConditioner())
                .hasWifi(busOperatorDto.getHasWifi())
                .hasToiletInBus(busOperatorDto.getHasToiletInBus())

                .hasUpperDeck(busOperatorDto.getHasUpperDeck())
                .hasStandardSeater(busOperatorDto.getHasStandardSeater())
                .hasSemiSleeper(busOperatorDto.getHasSemiSleeper())
                .hasSleeper(busOperatorDto.getHasSleeper())
                .totalSeatsCount(Long.parseLong(busOperatorDto.getTotalSeatsCount()))

                .lowerLeftSeaterType(busOperatorDto.getLowerLeftSeaterType())
                .lowerLeftSeatCountInEachRow(Long.parseLong(busOperatorDto.getLowerLeftSeatCountInEachRow()))
                .lowerLeftTotalRowsCount(Long.parseLong(busOperatorDto.getLowerLeftTotalRowsCount()))

                .lowerRightSeaterType(busOperatorDto.getLowerRightSeaterType())
                .lowerRightSeatCountInEachRow(Long.parseLong(busOperatorDto.getLowerRightSeatCountInEachRow()))
                .lowerRightTotalRowsCount(Long.parseLong(busOperatorDto.getLowerRightTotalRowsCount()))

                .upperTotalRowsCount(Long.parseLong(upperRowsCount))

                .hasRestStop(busOperatorDto.getHasRestStop())
                .restStopAtPlace(busOperatorDto.getRestStopAtPlace())
                .restStopTime(busOperatorDto.getRestStopTime())

                .seats(busOperatorDto.getSeats().stream().map(SeatMapper::toMapEntity).toList())
                .routes(busOperatorDto.getRoutes().stream().map(RouteMapper::toMapEntity).toList())
                .allBusStops(busOperatorDto.getAllBusStops().stream().map(StopMapper::toMapEntity).toList())
                .seatBookedStatus(busOperatorDto.getSeatBookedStatus().stream().map(BookingMapper::toMapEntity).toList())

                .baseFare(Float.valueOf(busOperatorDto.getBaseFare()))
                .insuranceFee(Float.valueOf(busOperatorDto.getInsuranceFee()))
                .build();
    }

    public static BusOperatorDto toMapDto(BusOperator busOperator) {

        return BusOperatorDto.builder()
                .id(busOperator.getId())
                .registerNumber(busOperator.getRegisterNumber())
                .name(busOperator.getName())
                .mobile(busOperator.getMobile().toString())
                .email(busOperator.getEmail())
                .operatorPassword(busOperator.getPassword())
                .driverMobile(busOperator.getDriverMobile().toString())

                .hasAirConditioner(busOperator.getHasAirConditioner())
                .hasWifi(busOperator.getHasWifi())
                .hasToiletInBus(busOperator.getHasToiletInBus())

                .hasUpperDeck(busOperator.getHasUpperDeck())
                .hasStandardSeater(busOperator.getHasStandardSeater())
                .hasSemiSleeper(busOperator.getHasSemiSleeper())
                .hasSleeper(busOperator.getHasSleeper())
                .totalSeatsCount(busOperator.getTotalSeatsCount().toString())

                .lowerLeftSeaterType(busOperator.getLowerLeftSeaterType())
                .lowerLeftSeatCountInEachRow(busOperator.getLowerLeftSeatCountInEachRow().toString())
                .lowerLeftTotalRowsCount(busOperator.getLowerLeftTotalRowsCount().toString())

                .lowerRightSeaterType(busOperator.getLowerRightSeaterType())
                .lowerRightSeatCountInEachRow(busOperator.getLowerRightSeatCountInEachRow().toString())
                .lowerRightTotalRowsCount(busOperator.getLowerRightTotalRowsCount().toString())

                .upperTotalRowsCount(busOperator.getUpperTotalRowsCount().toString())

                .hasRestStop(busOperator.getHasRestStop())
                .restStopAtPlace(busOperator.getRestStopAtPlace())
                .restStopTime(busOperator.getRestStopTime())

                .seats(busOperator.getSeats().stream().map(SeatMapper::toMapDto).toList())
                .routes(busOperator.getRoutes().stream().map(RouteMapper::toMapDto).toList())
                .allBusStops(busOperator.getAllBusStops().stream().map(StopMapper::toMapDto).toList())
                .seatBookedStatus(busOperator.getSeatBookedStatus().stream().map(BookingMapper::toMapDto).toList())

                .baseFare(busOperator.getBaseFare().toString())
                .insuranceFee(busOperator.getInsuranceFee().toString())
                .build();
    }
}

