package com.travels.bookmybus.controller;

import com.travels.bookmybus.dto.*;
import com.travels.bookmybus.model.Seat;
import com.travels.bookmybus.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookMyBus")
@SessionAttributes("selectedSeats")
public class PassengerController {
    private final PassengerService passengerService;
    private final SeatService seatService;
    private final StopService stopService;
    private final BusOperatorService busOperatorService;
    private final BookingService bookingService;

    List<Long> selectedSeats = new ArrayList<>();
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
        return "passenger/setPassword";
    }
    @GetMapping("/passengerPortal/{id}" )
    public String toHandlePassengerPortalGetRequest(@PathVariable("id") Long  passengerId,Model model){

        PassengerDto retrievedPassenger = passengerService.getById(passengerId);

        model.addAttribute("passenger", retrievedPassenger);
        model.addAttribute("searchDto",new SearchDto());
        model.addAttribute("mode","add");
        return "passenger/busRouteSearchForm";
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


        model.addAttribute("searchDto",searchDto);
        model.addAttribute("availableBuses",availableBusesInSearchedRoute);

        model.addAttribute("mode","add");
        return "passenger/showAvailableBus";
    }

@GetMapping("/passengerPortal/{busId}/seatsss")
public ResponseEntity<Map<String, Object>> getSeatsss(@PathVariable("busId") Long busOperatorId,
                                                      @RequestParam(name = "tripDate")LocalDate tripDate) {
    // Fetch seats associated with the bus operator
    List<SeatDto> seats = seatService.getSeatsByBusOperatorAndTripDate(busOperatorId,tripDate);


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
            @RequestParam("selectedSeats") String selectedSeats,Model model) {

        // Split seat numbers
        List<String> seatList = Arrays.asList(selectedSeats.split(","));
        model.addAttribute("selectedSeats", seatList);



        Long seatId = Long.parseLong(seatList.get(0));
        Long busId = seatService.getSeatEntityById(seatId).getBusOperator().getId();

        // Temporarily blocking these seats by saving it, In future Planning to add payment,
        // If payments failed, these seats will be released to book again

        // Process the seat booking
        boolean isBooked = passengerService.bookSeats(seatList);



        if (isBooked) {
            // Redirect or show confirmation message
            return "redirect:/bookMyBus/passengerPortal/"+id+"/selectPoints/"+busId;
        } else {
            // Show error message
            return "redirect:/bookingError";
        }
    }

    @GetMapping("/passengerPortal/{id}/selectPoints/{busId}")
    public String selectBoardingPoints(
            @PathVariable("id") Long passengerId,
            @PathVariable("busId") Long busId,
            Model model) {

        // Retrieve all stops for the bus
        List<StopDto> stops = stopService.getStopsForBus(busId);

        if (stops == null || stops.isEmpty()) {
            throw new IllegalArgumentException("No stops found for the given bus ID.");
        }

        // Determine the reference times for boarding and dropping points
        StopDto firstStop = stops.get(0);
        StopDto lastStop = stops.get(stops.size()-1);

        LocalTime boardingCutoff = firstStop.getLeavesAt().plusHours(2);
        LocalTime droppingCutoff = lastStop.getArrivesAt().minusHours(2);

        // Determine boarding and dropping points
        List<StopDto> boardingPoints = stops.stream()
                .filter(stop -> (
                                   (
                                     stop.getArrivesAt().isAfter( firstStop.getArrivesAt() ) ||
                                     stop.getArrivesAt() == ( firstStop.getArrivesAt() )
                                   )
                                   && stop.getArrivesAt().isBefore( boardingCutoff )
                                )
                ).collect(Collectors.toList());

        List<StopDto> droppingPoints = stops.stream()
                .filter( stop -> (
                                    (
                                        stop.getArrivesAt().isBefore(lastStop.getArrivesAt()) ||
                                        stop.getArrivesAt() == (lastStop.getArrivesAt())
                                    )
                                    && stop.getArrivesAt().isAfter(droppingCutoff)
                                 )
                ).collect(Collectors.toList());

        PassengerDto retrievedPassenger = passengerService.getById(passengerId);

        // Add data to the model
        model.addAttribute("boardingPoints", boardingPoints);
        model.addAttribute("droppingPoints", droppingPoints);
        model.addAttribute("busId", busId);
        model.addAttribute("passenger", retrievedPassenger);

        return "passenger/selectPoints";
    }


    @GetMapping("/passengerPortal/booking/addPassengerDetails")
    public String addPassengerDetailsForEachSeats(
            @RequestParam("passengerId") Long passengerId,
            @RequestParam("busId") Long busId,
            @RequestParam("boardingPoint") String selectedBoardingPoint,
            @RequestParam("droppingPoint") String selectedDroppingPoint,
            @ModelAttribute("selectedSeats") List<String> selectedSeats,
            Model model) {


        // Retrieve seat data
        List<SeatDto> seatsList = selectedSeats.stream()
                .map(s -> seatService.getById(Long.parseLong(s)))
                .toList();

        // Retrieve bus operator details
        BusOperatorDto busOperator = busOperatorService.getById(busId);

        // Parse the base fares and insurance fee from BusOperator
        double baseFareSeater = busOperator.getBaseFareSeaterType();
        double baseFareSemiSleeper = busOperator.getBaseFareSemiSleeperType();
        double baseFareSleeper = busOperator.getBaseFareSleeperType();
        double insuranceFee = busOperator.getInsuranceFee();

        // Calculate the total fare
        double totalFare = 0.0;
        for (SeatDto seat : seatsList) {
            double seatFare = 0.0;
            if ("standardSeater".equalsIgnoreCase(seat.getSeatType()) ) {
                seatFare = baseFareSeater;
            }
            else if ("semiSleeper".equalsIgnoreCase(seat.getSeatType())) {
                seatFare = baseFareSemiSleeper;
            }
            else if ("sleeper".equalsIgnoreCase(seat.getSeatType())) {
                seatFare = baseFareSleeper;
            }
            // Add insurance fee to the seat fare
            seatFare += insuranceFee;
            totalFare += seatFare;
        }

        // Add the calculated amount to the model
        model.addAttribute("amountToPay", totalFare);

        String seatNumbers = seatsList.stream()
                .map(SeatDto::getSeatNumber)
                .collect(Collectors.joining(", "));


        // Add attributes to the model
        model.addAttribute("seatsData", seatsList);
        model.addAttribute("seatNumbers", seatNumbers);

        model.addAttribute("passenger", passengerService.getById(passengerId));
        model.addAttribute("bus",busOperator );
        model.addAttribute("boardingPoint", selectedBoardingPoint);
        model.addAttribute("droppingPoint", selectedDroppingPoint);

        return "booking/addPassengerForEachSeat";
    }

    @PostMapping("/passengerPortal/booking/savePassengerDetails")
    public String savePassengerDetails(
            @RequestParam("busId") Long busId,
            @RequestParam("passengerId") Long passengerId,
            @RequestParam("boardingPoint") String selectedBoardingPoint,
            @RequestParam("droppingPoint") String selectedDroppingPoint,
            @RequestParam("seatNumbers") List<String> seatIds,
            @RequestParam("BookingFor") List<String> passengerNames,
            @RequestParam("genders") List<String> genders,
            @RequestParam("ages") List<Integer> ages,
            @RequestParam("primaryMobile") Long contactNumber,
            @RequestParam("secondaryMobile") Long secondaryNumber,
            Model model) {

        // Retrieve the bus and passenger details
        PassengerDto passenger = passengerService.getById(passengerId);
        BusOperatorDto busOperator = busOperatorService.getById(busId);

        List<BookingDto> bookings = new ArrayList<>();
        double totalFare = 0;
        // Loop through the selected seats and create booking entries
        for (int i = 0; i < seatIds.size(); i++) {
            // Calculate the fare for the seat
            double seatFare = 0;
            SeatDto seat = seatService.getById(Long.parseLong(seatIds.get(i)));


            if (seat.getSeatType().equals("standardSeater") ){
                seatFare = busOperator.getBaseFareSeaterType().doubleValue();
                System.out.println("\n\n\nCalculated value" + seatFare);
                System.out.println("Seat Type: " + seat.getSeatType());
                System.out.println("Base Fare for Seater: " + busOperator.getBaseFareSeaterType());
            }
            if (seat.getSeatType().equals("semiSleeper") ) {
                seatFare = busOperator.getBaseFareSemiSleeperType().doubleValue();

                System.out.println("\n\n\nCalculated value" + seatFare);
                System.out.println("Base Fare for Semi-Sleeper: " + busOperator.getBaseFareSemiSleeperType());
                System.out.println("Seat Type: " + seat.getSeatType());

            }
            if (seat.getSeatType().equals("sleeper") ) {


                seatFare = busOperator.getBaseFareSleeperType().doubleValue();

                System.out.println("\n\n\nCalculated value" + seatFare);
                System.out.println("Seat Type: " + seat.getSeatType());
                System.out.println("Base Fare for sleeper: " + busOperator.getBaseFareSleeperType());

            }
            // Add insurance fee
            seatFare += busOperator.getInsuranceFee();
            System.out.println("\n\n\n\nseat fare with insurance is : " + seatFare);

            totalFare += seatFare;

            LocalDate droppingDate = seat.getTripDate(); // Original trip date for boarding
            LocalTime droppingTime = passengerService.extractTimeFromPoint(selectedDroppingPoint);
            // When booking time is close to the end of day, need to update dropping date to the next date
            if (droppingTime.isAfter(LocalTime.of(23, 59))) {
                // Add 1 day to the trip date if the boarding time is after 23:59
                droppingDate = droppingDate.plusDays(1);
            }

            // Create BookingDto for each seat
            BookingDto booking = BookingDto.builder()
                    .seatNumber(seat.getSeatNumber())
                    .seatType(seat.getSeatType())
                    .seatFare(seatFare)
                    .passengerName(passengerNames.get(i))
                    .age(ages.get(i))
                    .gender(genders.get(i))
                    .contactNumber(contactNumber)
                    .secondaryNumber(secondaryNumber)
                    .email(passenger.getEmail())
                    .bookedDate(LocalDate.now())
                    .bookedTime(LocalTime.now())
                    .passengerBoardingPlace(selectedBoardingPoint)
                    .passengerBoardingDate(seat.getTripDate()) // Adjust as needed
                    .passengerBoardingTime(passengerService.extractTimeFromPoint(selectedBoardingPoint))
                    .passengerDroppingPlace(selectedDroppingPoint)
                    .passengerDroppingDate(droppingDate) // Adjust as needed
                    .passengerDroppingTime(droppingTime)
                    .busDepartureLocation(busOperator.getRoutes().get(0).getDeparturePlace())
                    .busArrivalLocation(busOperator.getRoutes().get(0).getArrivalPlace())
                    .passengerId(passengerId)
                    .busId(busId)
                    .seatId(seat.getId())
                    .build();

            bookings.add(booking);
            System.out.println(" \n\n\nBooking " + i + ":\t" + booking);
        }


        // Save bookings in the database
        bookingService.saveAll(bookings);

        // Redirect to a success page or confirmation page
        model.addAttribute("bookings", bookings);
        model.addAttribute("totalFare", totalFare);
        model.addAttribute("successMessage", "Your booking was successful!!! Please find your ticket details below...");
        model.addAttribute("passenger",passenger);
        model.addAttribute("bus",busOperator);

        return "booking/confirmation";
    }

    @GetMapping("/passengerPortal/{id}/bookedTickets")
    public String getBookedTickets(
            @PathVariable Long id,
            Model model
    ) {
        List<BookingDto> bookedTickets = bookingService.getBookedTicketsForPassenger(id);

        List<GroupedBookingDto> groupedBookings = bookedTickets.stream()
                .collect(Collectors.groupingBy(BookingDto::getPassengerBoardingDate))
                .entrySet().stream()
                .map(entry -> {
                    List<BookingDto> bookings = entry.getValue();
                    double totalFare = bookings.stream()
                            .mapToDouble(BookingDto::getSeatFare)
                            .sum();

                    BookingDto firstBooking = bookings.get(0);
                    BusOperatorDto busOperatorDto = busOperatorService.getById(firstBooking.getBusId());

                    GroupedBookingDto dto = new GroupedBookingDto();
                    dto.setBookingDate(entry.getKey());
                    dto.setRoute(firstBooking.getBusDepartureLocation() + " to " + firstBooking.getBusArrivalLocation());
                    dto.setBusOperatorName(busOperatorDto.getName());
                    dto.setBusNumber(busOperatorDto.getRegisterNumber());
                    dto.setDriverContact(busOperatorDto.getDriverMobile());
                    dto.setBoardingPoint(firstBooking.getPassengerBoardingPlace());
                    dto.setBoardingDate(firstBooking.getPassengerBoardingDate());
                    dto.setBoardingTime(String.valueOf(firstBooking.getPassengerBoardingTime()));
                    dto.setDroppingPoint(firstBooking.getPassengerDroppingPlace());
                    dto.setDroppingDate(firstBooking.getPassengerDroppingDate());
                    dto.setDroppingTime(String.valueOf(firstBooking.getPassengerDroppingTime()));
                    dto.setBookings(bookings);
                    dto.setTotalFare(totalFare);

                    return dto;
                })
                .sorted(Comparator.comparing(GroupedBookingDto::getBookingDate).reversed())
                .collect(Collectors.toList());

        model.addAttribute("groupedBookings", groupedBookings);
        model.addAttribute("passenger", passengerService.getById(id));

        return "booking/bookedHistory";
    }

}
