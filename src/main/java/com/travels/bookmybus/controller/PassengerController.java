package com.travels.bookmybus.controller;

import com.travels.bookmybus.dto.*;
import com.travels.bookmybus.model.Seat;
import com.travels.bookmybus.service.BusOperatorService;
import com.travels.bookmybus.service.PassengerService;
import com.travels.bookmybus.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookMyBus")
public class PassengerController {
    private final PassengerService passengerService;
    private final SeatService seatService;
    private final BusOperatorService busOperatorService;
    @GetMapping("/addPassenger")
    public String toHandleAddPassengerRequest(Model model){
        PassengerDto passengerDto = new PassengerDto();
        model.addAttribute("passenger",passengerDto);
        model.addAttribute("mode","add");
        return "passenger/passengerForm";
    }
    @PostMapping("/addPassenger")
    public String toHandleAddPassenger( @Valid @ModelAttribute("passenger") PassengerDto passenger,
                                   BindingResult result,
                                   Model model){
        model.addAttribute("mode","add");
        //Handle Validation Error if exists
        if(result.hasErrors()){
//            result.getAllErrors().forEach(error -> System.out.println(error + error.getDefaultMessage()));
            return "passenger/passengerForm";
        }

        //Handle Error if mail ID already exist
        if( passengerService.isExistByEmailId(passenger.getEmail()) ){
            model.addAttribute("errorExistEmail","Passenger Email ID already exist");
            return "passenger/passengerForm";
        }

        PassengerDto savedPassenger = passengerService.savePassenger(passenger);

        return "redirect:/bookMyBus/addPassenger/setPassword?savedPassengerId=" + savedPassenger.getId();
    }

    @GetMapping("/addPassenger/setPassword")
    public String toHandleSetPasswordRequest( @RequestParam("savedPassengerId") Long passengerId,
                                              Model model){
        model.addAttribute("mode","add");
        model.addAttribute("credentials",new CredentialDto());
        model.addAttribute("savedPassengerId",passengerId);
        return "passenger/setPassword";
    }
    @PostMapping("/addPassenger/setPassword")
    public String toHandleSetPassword(   @Valid @ModelAttribute("credentials") CredentialDto credentials,
                                         BindingResult result,
                                         @RequestParam("savedPassengerId") Long passengerId,
                                         Model model){
        model.addAttribute("mode","add");
        model.addAttribute("savedPassengerId", passengerId);
        if(result.hasErrors()){
            return "passenger/setPassword";
        }
        // To check password fields are matching
        if( ! credentials.getPassword().equals( credentials.getConfirmPassword() ) ) {
            model.addAttribute("error","Passwords are not matching");
            return "passenger/setPassword";
        }

        PassengerDto retrievedPassenger = passengerService.getById(passengerId);
        retrievedPassenger.setPassword(credentials.getConfirmPassword());
        passengerService.savePassenger(retrievedPassenger);

        // For showing added notification and redirect to patient Home page
        model.addAttribute("success", true);
//        return "redirect:/bookMyBus/passengerPortal/" + passengerId;
        return "passenger/setPassword";
    }
    @GetMapping("/passengerPortal/{id}" )
    public String toHandlePassengerPortalGetRequest(@PathVariable("id") Long  passengerId,Model model){

        PassengerDto retrievedPassenger = passengerService.getById(passengerId);

        model.addAttribute("passenger", retrievedPassenger);
        model.addAttribute("mode","view");
        return "passenger/busBooking";
    }
    @GetMapping("/passengerPortal/{id}/update" )
    public String toHandlePassengerPortalUpdateProfileRequest(@PathVariable("id") Long  passengerId,Model model){

        PassengerDto retrievedPassenger = passengerService.getById(passengerId);

        model.addAttribute("passenger", retrievedPassenger);
        model.addAttribute("mode","update");
        return "passenger/passengerForm";
    }
    @PutMapping("/passengerPortal/{id}/update" )
    public String toHandlePassengerPortalUpdateProfilePostRequest( @Valid @ModelAttribute("passenger") PassengerDto updatePassenger,
                                                                   BindingResult result,Model model){
        model.addAttribute("mode","update");

        if(result.hasErrors()){
            return "passenger/passengerForm";
        }
        PassengerDto updatedPassenger = passengerService.savePassenger(updatePassenger);

        model.addAttribute("passenger",updatedPassenger );
        // For showing update notification and redirect to passenger Bus booking page
        model.addAttribute("updateSuccess", true);

        return "passenger/passengerForm";
    }
    @GetMapping("/passengerPortal/{id}/searchBus" )
    public String toHandlePassengerPortalSearchBusRequest(@PathVariable("id") Long  passengerId,Model model){

        PassengerDto retrievedPassenger = passengerService.getById(passengerId);

        model.addAttribute("passenger", retrievedPassenger);
        model.addAttribute("searchDto",new SearchDto());
        model.addAttribute("mode","add");
        return "passenger/busRouteSearchForm";
    }
    @PostMapping("/passengerPortal/{id}/searchBus" )
    public String toHandlePassengerPortalSearchBus(@PathVariable("id") Long  passengerId,
                                                   @Valid @ModelAttribute("searchDto") SearchDto searchDto,
                                                   BindingResult result,Model model){

        PassengerDto retrievedPassenger = passengerService.getById(passengerId);
        model.addAttribute("passenger", retrievedPassenger);

        if (result.hasErrors())
            return "passenger/busRouteSearchForm";

        List<BusOperatorDto> availableBusesInSearchedRoute = passengerService.
                getBusesByRoute(searchDto,passengerId);

        System.out.println("After cnverting to Dto's " + availableBusesInSearchedRoute);
        model.addAttribute("searchDto",searchDto);
        model.addAttribute("availableBuses",availableBusesInSearchedRoute);
        model.addAttribute("mode","add");
        return "passenger/showAvailableBus";
    }
    @GetMapping("/passengerPortal/{id}/seats")
    public ResponseEntity<List<SeatDto>> getSeats(@PathVariable("id") Long busOperatorId) {
        try {
            List<SeatDto> seats = seatService.getSeatsByBusOperator(busOperatorId);
            System.out.println("\n\n\nAJAX Call: \n" + seats);
            return ResponseEntity.ok(seats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/passengerPortal/{busId}/seatss")
    public ResponseEntity<Map<String, List<Seat>>> getSeatss(@PathVariable("busId") Long busId) {
        List<Seat> allSeats = seatService.getSeatsEntityByBusOperator(busId);

        // Separate the seats based on their deck (lower/upper) and side (left/right)
        Map<String, List<Seat>> seatsMap = new HashMap<>();

        // Group seats by their categories (lowerLeft, lowerRight, upperLeft, upperRight)
        seatsMap.put("lowerLeft", allSeats.stream()
                .filter(seat -> seat.getSeatNumber().startsWith("L") && seat.getSeatNumber().charAt(seat.getSeatNumber().length() - 2) == 'L')
                .collect(Collectors.toList()));

        seatsMap.put("lowerRight", allSeats.stream()
                .filter(seat -> seat.getSeatNumber().startsWith("L") && seat.getSeatNumber().charAt(seat.getSeatNumber().length() - 2) == 'R')
                .collect(Collectors.toList()));

        seatsMap.put("upperLeft", allSeats.stream()
                .filter(seat -> seat.getSeatNumber().startsWith("U") && seat.getSeatNumber().charAt(seat.getSeatNumber().length() - 2) == 'L')
                .collect(Collectors.toList()));

        seatsMap.put("upperRight", allSeats.stream()
                .filter(seat -> seat.getSeatNumber().startsWith("U") && seat.getSeatNumber().charAt(seat.getSeatNumber().length() - 2) == 'R')
                .collect(Collectors.toList()));

        return ResponseEntity.ok(seatsMap);
    }
//    @GetMapping("/passengerPortal/{busId}/seatsss")
//    public ResponseEntity<Map<String, Object>> getSeatsss(@PathVariable("busId") Long busOperatorId) {
//        // Fetch seats associated with the bus operator
//        List<SeatDto> seats = seatService.getSeatsByBusOperator(busOperatorId);
//
//        // Separate seats into lower-left, lower-right, upper-left, and upper-right categories
//        Map<String, List<SeatDto>> seatsMap = new HashMap<>();
//        seatsMap.put("lowerLeft", seats.stream()
//                .filter(seat -> seat.getSeatNumber().startsWith("L") && seat.getSeatNumber().endsWith("L"))
//                .collect(Collectors.toList()));
//        seatsMap.put("lowerRight", seats.stream()
//                .filter(seat -> seat.getSeatNumber().startsWith("L") && seat.getSeatNumber().endsWith("R"))
//                .collect(Collectors.toList()));
//        seatsMap.put("upperLeft", seats.stream()
//                .filter(seat -> seat.getSeatNumber().startsWith("U") && seat.getSeatNumber().endsWith("L"))
//                .collect(Collectors.toList()));
//        seatsMap.put("upperRight", seats.stream()
//                .filter(seat -> seat.getSeatNumber().startsWith("U") && seat.getSeatNumber().endsWith("R"))
//                .collect(Collectors.toList()));
//
//        // Fetch the bus operator's configuration details
//        BusOperatorDto busOperatorDto = busOperatorService.getById(busOperatorId);
//
//        // Seat configuration derived from the bus operator
//        Map<String, Object> config = Map.of(
//                "lowerLeftSeatCountInEachRow", Integer.parseInt(busOperatorDto.getLowerLeftSeatCountInEachRow()),
//                "lowerLeftTotalRowsCount", Integer.parseInt(busOperatorDto.getLowerLeftTotalRowsCount()),
//                "lowerRightSeatCountInEachRow", Integer.parseInt(busOperatorDto.getLowerRightSeatCountInEachRow()),
//                "lowerRightTotalRowsCount", Integer.parseInt(busOperatorDto.getLowerRightTotalRowsCount()),
//                "upperLeftSeatCountInEachRow", 1, // Default value, can be overridden
//                "upperRightSeatCountInEachRow", 2, // Default value, can be overridden
//                "upperTotalRowsCount", Integer.parseInt(busOperatorDto.getUpperTotalRowsCount())
//        );
//
//        // Construct the final response
//        Map<String, Object> response = new HashMap<>();
//        response.put("seats", seatsMap);
//        response.put("config", config);
//
//        return ResponseEntity.ok(response);
//    }
@GetMapping("/passengerPortal/{busId}/seatsss")
public ResponseEntity<Map<String, Object>> getSeatsss(@PathVariable("busId") Long busOperatorId) {
    // Fetch seats associated with the bus operator
    List<SeatDto> seats = seatService.getSeatsByBusOperator(busOperatorId);
        seats.get(0).setIsBooked(true);
    System.out.println( "\n\n\nAfter Manual setup changing \t:" + seats.get(0));
        seats.get(4).setIsBooked(true);
        seats.get(15).setIsBooked(true);
    // Separate seats into lower-left, lower-right, upper-left, and upper-right categories
    Map<String, List<SeatDto>> seatsMap = new HashMap<>();
    seatsMap.put("lowerLeft", seats.stream()
            .filter(seat -> seat.getSeatNumber().startsWith("L") && seat.getSeatNumber().charAt(seat.getSeatNumber().length() - 2) == 'L')
            .collect(Collectors.toList()));
    seatsMap.put("lowerRight", seats.stream()
            .filter(seat -> seat.getSeatNumber().startsWith("L") && seat.getSeatNumber().charAt(seat.getSeatNumber().length() - 2) == 'R' )
            .collect(Collectors.toList()));
    seatsMap.put("upperLeft", seats.stream()
            .filter(seat -> seat.getSeatNumber().startsWith("U") && seat.getSeatNumber().charAt(seat.getSeatNumber().length() - 2) == 'L')
            .collect(Collectors.toList()));
    seatsMap.put("upperRight", seats.stream()
            .filter(seat -> seat.getSeatNumber().startsWith("U") && seat.getSeatNumber().charAt(seat.getSeatNumber().length() - 2) == 'R')
            .collect(Collectors.toList()));

    // Fetch the bus operator's configuration details
    BusOperatorDto busOperatorDto = busOperatorService.getById(busOperatorId);

    // Combine seats and configuration into a single response map
    Map<String, Object> response = new HashMap<>();
    response.put("seats", seatsMap); // Seats categorized into lower/upper and left/right
    response.put("lowerLeftSeatCountInEachRow", Integer.parseInt(busOperatorDto.getLowerLeftSeatCountInEachRow()));
    response.put("lowerLeftTotalRowsCount", Integer.parseInt(busOperatorDto.getLowerLeftTotalRowsCount()));
    response.put("lowerRightSeatCountInEachRow", Integer.parseInt(busOperatorDto.getLowerRightSeatCountInEachRow()));
    response.put("lowerRightTotalRowsCount", Integer.parseInt(busOperatorDto.getLowerRightTotalRowsCount()));
    response.put("upperLeftSeatCountInEachRow", 1); // Default value
    response.put("upperRightSeatCountInEachRow", 2); // Default value
    response.put("upperTotalRowsCount", Integer.parseInt(busOperatorDto.getUpperTotalRowsCount()));


    return ResponseEntity.ok(response);
}

    @PostMapping("/passengerPortal/{busId}/bookSeats")
    public ResponseEntity<?> bookSeats(@PathVariable Long busId,
                                       @RequestBody SeatBookingRequestDto request) {
        try {
            // Call a service method to book the seats
            List<Seat> updatedSeats = seatService.bookSeats(busId, request.getSeats());
            return ResponseEntity.ok(updatedSeats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to book seats: " + e.getMessage());
        }
    }

    @PostMapping("/passengerPortal/{id}/selectSeats")
    public String selectSeats(
            @PathVariable("id") Long id, // Passenger ID
            @RequestParam("selectedSeats") String selectedSeats) {

        List<String> seatList = Arrays.asList(selectedSeats.split(","));

        // Log for debugging
        System.out.println("\n\n\nPassenger ID: " + id);
        System.out.println("Selected Seats: " + selectedSeats +"\n\n\n");

        // Process the seat booking
        boolean isBooked = passengerService.bookSeats(seatList);

        if (isBooked) {
            // Redirect or show confirmation message
            return "redirect:/bookingConfirmation";
        } else {
            // Show error message
            return "redirect:/bookingError";
        }
    }
}
