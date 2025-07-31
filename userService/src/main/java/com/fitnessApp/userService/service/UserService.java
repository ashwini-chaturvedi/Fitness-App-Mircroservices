package com.fitnessApp.userService.service;

import com.fitnessApp.userService.dto.RegisterRequest;
import com.fitnessApp.userService.dto.UserResponse;
import com.fitnessApp.userService.entity.User;
import com.fitnessApp.userService.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setUserId(user.getUserId());
        userResponse.setKeycloakId(user.getKeycloakId());
        userResponse.setEmailId(user.getEmailId());
        userResponse.setPassword(user.getPassword());
        userResponse.setUserName(user.getUserName());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhoneNo(user.getPhoneNo());
        userResponse.setDOB(user.getDOB());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }

    public UserResponse register(@Valid RegisterRequest request) throws Exception {

        if (this.userRepository.existsByEmailId(request.getEmailId())) {

            User existingUser = this.userRepository.findByEmailId(request.getEmailId());
            mapUserToUserResponse(existingUser);
        }

        User user = new User();

        user.setEmailId(request.getEmailId());
        user.setKeycloakId(request.getKeycloakId());
        user.setPassword(request.getPassword());
        user.setUserName(request.getUserName());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNo(request.getPhoneNo());
        user.setDOB(request.getDOB());

        User savedUser = this.userRepository.save(user);


        return mapUserToUserResponse(savedUser);
    }


    public UserResponse getUserProfile(String userId) {
        User existingUser = this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not " +
                "Found"));

        return mapUserToUserResponse(existingUser);
    }


    public Boolean existByUserId(String userId) {
        return this.userRepository.existsById(userId);
    }

    public Boolean existsByKeycloakId(String keycloakId) {
        return this.userRepository.existsByKeycloakId(keycloakId);
    }

    public UserResponse getUserProfileWithKeycloakId(String keycloakId) {
        User existingUser = this.userRepository.findByKeycloakId(keycloakId);
        return mapUserToUserResponse(existingUser);
    }
}
