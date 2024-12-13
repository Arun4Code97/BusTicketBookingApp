package com.travels.bookmybus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PassengerDto {

    private Long id;

    @NotBlank(message = "Field should not be blank")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z ,.'-]{1,29}$", message = "First character must be a letter and be up to 30 characters: {A-Z,a-z., '-}")
    private String firstName;

    @NotBlank(message = "Field should not be blank")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z ,.'-]{1,29}$", message = "First character must be a letter and be up to 30 characters: {A-Z,a-z., '-}")
    private String lastName;

    @NotNull(message = "Field should not be null")
    @Pattern(regexp = "^[0-9]{1,3}$", message = "Enter valid number up to 3 digits")
    private String age;

    @NotBlank(message = "Field should not be blank")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z ]{1,15}$", message = "First character must be a letter and be up to 30 characters: {A-Z,a-z }")
    private String gender;

    @NotBlank(message = "Field should not be blank")
    @Email(message = "Enter valid email id")
    private String email;

    @NotNull(message = "Field should not be null")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Should start with above 6 and contain exactly 10 digits.")
    private String primaryMobile;

    @NotNull(message = "Field should not be null")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Should start with above 6 and contain exactly 10 digits.")
    private String secondaryMobile;

    @NotBlank(message = "Field should not be blank")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z ]{1,15}$", message = "First character must be a letter and be up to 15 characters: {A-Z,space,a-z}")
    private String city;

    @NotBlank(message = "Field should not be blank")
    @Pattern(regexp = "^[A-Za-z0-9\\-.,:/()#& ]{1,255}$", message = "Allowed characters (A-Z,a-z,0-9), (-, ./:()#&)  and up to 256 characters.")
    private String address;

    private String password;
}
