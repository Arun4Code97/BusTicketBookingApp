package com.travels.bookmybus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
    @NotBlank(message = "User Type should not be blank")
    private String userType;

    @Email(message = "email ID should be valid ")
    @NotBlank(message = "email ID should not be blank")
    private String email;

    @NotBlank(message = "Password should not be blank")
    private String password;
}
