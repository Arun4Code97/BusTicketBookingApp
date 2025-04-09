package com.travels.bookmybus.controller;

import com.travels.bookmybus.dto.BusOperatorDto;
import com.travels.bookmybus.dto.PassengerDto;
import com.travels.bookmybus.dto.UserLoginDto;
import com.travels.bookmybus.service.BusOperatorService;
import com.travels.bookmybus.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = {"/","/bookMyBus"})
public class HomeController {
    private final PassengerService passengerService;
    private final BusOperatorService busOperatorService;
    @GetMapping({"/login",""})
    public String toHandleHomePageRequest(Model model){
        UserLoginDto userData = new UserLoginDto();
        userData.setUserType("passenger"); // Set default value to passenger
        model.addAttribute("userData",userData);
        return "utility/homePage";
    }
    @PostMapping("/login")
    public String toHandleLoginRequest(@Valid @ModelAttribute("userData") UserLoginDto userData,
                                       BindingResult result, Model model) {
        // If validation errors exist, return to home page
        if (result.hasErrors()) {
            return "utility/homePage";
        }
        // Handle login based on user type
        if ("passenger".equals(userData.getUserType())) {
            return handlePassengerLogin(userData, model);
        } else if ("operator".equals(userData.getUserType())) {
            return handleOperatorLogin(userData, model);
        } else {
            model.addAttribute("error", "Invalid user type.");
            return "utility/homePage";
        }
    }

    // Handle patient login
    private String handlePassengerLogin(UserLoginDto userData, Model model) {

        if (passengerService.isExistByEmailId(userData.getEmail())) {
            PassengerDto existPassenger = passengerService.getPassengerByEmailId(userData.getEmail());
                if (existPassenger.getPassword().equals(userData.getPassword())) {
                    return "redirect:/bookMyBus/passengerPortal/" + existPassenger.getId();
                } else {
                    model.addAttribute("error", "Incorrect password.");
                }
        } else {
            model.addAttribute("error", "Email ID " + userData.getEmail() + " does not exist.");
        }
        return "utility/homePage";
    }

//    // Handle doctor login
    private String handleOperatorLogin(UserLoginDto userData, Model model) {

        if (busOperatorService.isExistByEmailId(userData.getEmail())) {
            BusOperatorDto existOperator = busOperatorService.getOperatorByEmailId(userData.getEmail());
                if (existOperator.getOperatorPassword().equals(userData.getPassword())) {
                    return "redirect:/bookMyBus/busOperatorPortal/" + existOperator.getId();
                } else {
                    model.addAttribute("error", "Incorrect password.");
                }
        } else {
            model.addAttribute("error", "Email ID " + userData.getEmail() + " does not exist.");
        }
        return "utility/homePage";
    }
}
