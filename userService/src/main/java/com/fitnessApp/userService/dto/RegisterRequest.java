package com.fitnessApp.userService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email Id is Required")
    @Email(message = "Invalid Email Format")
    private String emailId;
    private String keycloakId;
    private String userName;

    @NotBlank(message = "Password cannot be Blank!!!")
    @Size(min = 8, message = "Password must have least 8 Characters")
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private LocalDateTime DOB;

}
