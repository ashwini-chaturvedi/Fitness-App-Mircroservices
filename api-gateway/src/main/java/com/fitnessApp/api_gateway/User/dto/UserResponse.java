package com.fitnessApp.api_gateway.User.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

    private String userId;
    private String keycloakId;
    private String emailId;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private LocalDateTime DOB;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
