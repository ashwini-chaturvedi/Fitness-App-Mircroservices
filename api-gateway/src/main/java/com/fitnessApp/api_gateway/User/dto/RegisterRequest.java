package com.fitnessApp.api_gateway.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email Id is Required")
    @Email(message = "Invalid Email Format")
    private String emailId;
    private String keycloakId;

    @NotBlank(message = "Password cannot be Blank!!!")
    @Size(min = 8, message = "Password must have least 8 Characters")
    private String password;
    private String firstName;
    private String lastName;

}
