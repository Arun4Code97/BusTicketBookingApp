package com.travels.bookmybus.controller;

import com.travels.bookmybus.dto.*;
import com.travels.bookmybus.service.BusOperatorService;
import com.travels.bookmybus.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookMyBus")
public class BusOperatorController {
    private final BusOperatorService busOperatorService;

    @GetMapping("/addBusOperator")
    public String toHandleAddBusOperatorRequest(Model model){
        BusOperatorDto busOperatorDto = new BusOperatorDto();
        model.addAttribute("busOperator", busOperatorDto);
        model.addAttribute("mode","add");
        return "operator/operatorFormNew";
    }
    @PostMapping("/addBusOperator")
    public String toHandleAddBusOperator( @Valid @ModelAttribute("busOperator") BusOperatorDto busOperatorDto,
                                        BindingResult result,
                                        Model model){

        model.addAttribute("mode","add");
        //Handle Validation Error if exists
        if(result.hasErrors()){
//            result.getAllErrors().forEach(error -> System.out.println(error + error.getDefaultMessage()));
            return "operator/operatorFormNew";
        }

        //Handle Error if mail ID already exist
        if( busOperatorService.isExistByEmailId(busOperatorDto.getEmail()) ){
            model.addAttribute("errorExistEmail","Email ID already exist");
            return "operator/operatorFormNew";
        }

        BusOperatorDto savedBusOperator = busOperatorService.saveBus(busOperatorDto);

        System.out.println("saved bus operator\n\n\n" + savedBusOperator + "\n\n");

        return "redirect:/bookMyBus/addBusOperator/setPassword?savedBusOperatorId=" + savedBusOperator.getId();
    }

    @GetMapping("/addBusOperator/setPassword")
    public String toHandleSetPasswordRequest( @RequestParam("savedBusOperatorId") Long busOperatorId,
                                              Model model){
        model.addAttribute("mode","add");
        model.addAttribute("credentials",new CredentialDto());
        model.addAttribute("savedBusOperatorId",busOperatorId);
        return "operator/setPassword";
    }
    @PostMapping("/addBusOperator/setPassword")
    public String toHandleSetPassword(   @Valid @ModelAttribute("credentials") CredentialDto credentials,
                                         BindingResult result,
                                         @RequestParam("savedBusOperatorId") Long busOperatorId,
                                         Model model){
        model.addAttribute("mode","add");
        model.addAttribute("savedBusOperatorId", busOperatorId);
        if(result.hasErrors()){
            return "operator/setPassword";
        }
        // To check password fields are matching
        if( ! credentials.getPassword().equals( credentials.getConfirmPassword() ) ) {
            model.addAttribute("error","Passwords are not matching");
            return "operator/setPassword";
        }

        BusOperatorDto retrievedBusOperator = busOperatorService.getById(busOperatorId);
        retrievedBusOperator.setOperatorPassword(credentials.getConfirmPassword());
        busOperatorService.saveBus(retrievedBusOperator);

        // For showing added notification and redirect to patient Home page
        model.addAttribute("success", true);
//        return "redirect:/bookMyBus/passengerPortal/" + passengerId;
        return "operator/setPassword";
    }

    @GetMapping("/busOperatorPortal/{id}" )
    public String toHandleBusOperatorPortalGetRequest(@PathVariable("id") Long  busOperatorId,Model model){

        BusOperatorDto retrievedBusOperator = busOperatorService.getById(busOperatorId);

        model.addAttribute("busOperator", retrievedBusOperator);
        model.addAttribute("mode","view");
        return "operator/operatorFormNew";
    }
    @GetMapping("/busOperatorPortal/{id}/update" )
    public String toHandleBusOperatorPortalUpdateProfileRequest(@PathVariable("id") Long  busOperatorId,Model model){

        BusOperatorDto retrievedBusOperator = busOperatorService.getById(busOperatorId);

        model.addAttribute("busOperator", retrievedBusOperator);
        model.addAttribute("mode","update");
        return "operator/operatorFormNew";
    }
    @PutMapping("/busOperatorPortal/{id}/update" )
    public String toHandleBusOperatorPortalUpdateProfilePostRequest( @Valid @ModelAttribute("busOperator") BusOperatorDto updateBusOperator,
                                                                   BindingResult result,Model model){
        model.addAttribute("mode","update");

        if(result.hasErrors()){
            return "operator/operatorFormNew";
        }
        BusOperatorDto updatedBusOperator = busOperatorService.saveBus(updateBusOperator);

        model.addAttribute("busOperator",updatedBusOperator );
        // For showing update notification and redirect to passenger Bus booking page
        model.addAttribute("updateSuccess", true);

        return "operator/operatorFormNew";
    }
    @GetMapping("/busOperatorPortal/{id}/seatSetup" )
    public String toHandleBusOperatorPortalSeatSetupRequest(@PathVariable("id") Long  busOperatorId,Model model){

        BusOperatorDto retrievedBusOperator = busOperatorService.getById(busOperatorId);
        model.addAttribute("busOperator", retrievedBusOperator);
        model.addAttribute("mode","add");
        return "operator/seatSetup";
    }
    @PostMapping("/busOperatorPortal/seatSetup")
    public String handleSeatSetupSubmission(
            @RequestParam("seatNumbers[]") List<String> seatNumbers,
            @RequestParam("busOperatorId") Long busOperatorId,
            RedirectAttributes redirectAttributes) {


        // Save seat configuration
        busOperatorService.saveSeatConfiguration(busOperatorId, seatNumbers);

        // Add success message
        redirectAttributes.addFlashAttribute("successMessage", "Seat configuration saved successfully!");
        return "redirect:/bookMyBus/busOperatorPortal/" + busOperatorId + "/seatSetup";
    }
    @GetMapping("/busOperatorPortal/{id}/routeSetup" )
    public String toHandleBusOperatorPortalRouteSetupRequest(@PathVariable("id") Long  busOperatorId,Model model){

        BusOperatorDto retrievedBusOperator = busOperatorService.getById(busOperatorId);
        model.addAttribute("busOperator", retrievedBusOperator);
        model.addAttribute("route", new RouteDto());
        model.addAttribute("mode","add");
        return "operator/routeSetup";
    }
    @PostMapping("/busOperatorPortal/{id}/routeSetup")
    public String handleRouteSetupSubmission(
            @PathVariable("id") Long busOperatorId,
            @Valid @ModelAttribute("route") RouteDto routeDto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        BusOperatorDto retrievedBusOperator = busOperatorService.getById(busOperatorId);
        model.addAttribute("busOperator", retrievedBusOperator);

        if (result.hasErrors()) {
            return "operator/routeSetup";
        }
        System.out.println("\n\n\n\nIncoming busOperatorId is : \t\t" + busOperatorId);
        // Save seat configuration

        busOperatorService.saveRouteConfiguration(busOperatorId, routeDto);

//        System.out.println("\n\n\n\nAfter saved :\n \t\t" + busOperatorService.saveRouteConfiguration(busOperatorId, routeDto));
        // Add success message
        redirectAttributes.addFlashAttribute("successMessage", "Route configuration saved successfully!");
        return "redirect:/bookMyBus/busOperatorPortal/" + busOperatorId;
    }
    @GetMapping("/busOperatorPortal/{id}/stopsSetup")
    public String toHandleBusOperatorPortalStopSetupRequest(
            @PathVariable("id") Long busOperatorId,
            Model model) {

        BusOperatorDto retrievedBusOperator = busOperatorService.getById(busOperatorId);

        // Prepare an empty form with a placeholder StopDto
        StopSetupForm stopSetupForm = new StopSetupForm();
        stopSetupForm.getStopDtos().add(new StopDto());  // Add placeholder for the form

        model.addAttribute("busOperator", retrievedBusOperator);
        model.addAttribute("stopSetupForm", stopSetupForm);  // Pass the form object instead
        model.addAttribute("mode", "add");
        return "operator/stopSetup";
    }

    @PostMapping("/busOperatorPortal/{id}/stopsSetup")
    public String stopsSetup(
            @PathVariable("id") Long busOperatorId,
            @ModelAttribute("stopSetupForm") StopSetupForm stopSetupForm,
            RedirectAttributes redirectAttributes) {

        List<StopDto> stopDtos =  new ArrayList<>();
        stopDtos = stopSetupForm.getStopDtos();


        if (stopDtos == null || stopDtos.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please add at least one stop.");
            return "redirect:/busOperatorPortal/" + busOperatorId + "/stopsSetup";
        }

        // Save stop configuration to the service
        busOperatorService.saveStopConfiguration(busOperatorId, stopDtos);

        // Redirect with a success message
        redirectAttributes.addFlashAttribute("successMessage", "Stops saved successfully!");
        return "redirect:/bookMyBus/busOperatorPortal/" + busOperatorId;
    }
//    @GetMapping("/seats/{busOperatorId}")
//    public ResponseEntity<List<SeatDto>> getSeats(@PathVariable Long busOperatorId) {
//        try {
//            List<SeatDto> seats = seatService.getSeatsByBusOperator(busOperatorId);
//            System.out.println("\n\n\nAJAX Call: \n" + seats);
//            return ResponseEntity.ok(seats);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }


}
