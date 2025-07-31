package com.fitnessApp.userService.controller;

import com.fitnessApp.userService.dto.RegisterRequest;
import com.fitnessApp.userService.dto.UserResponse;
import com.fitnessApp.userService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> setUserProfile(@Valid @RequestBody RegisterRequest request) throws Exception {

        try {
            return ResponseEntity.ok(userService.register(request));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(this.userService.getUserProfile(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/keycloak/{keycloakId}")
    public ResponseEntity<?> getUserProfileWithKeycloakId(@PathVariable String keycloakId) {
        try {
            return ResponseEntity.ok(this.userService.getUserProfileWithKeycloakId(keycloakId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/{keycloakId}/keycloak/validate")
    public ResponseEntity<?> interServiceCommunication1(@PathVariable String keycloakId) {
        try {
            return ResponseEntity.ok(this.userService.existsByKeycloakId(keycloakId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/{userId}/validate")
    public ResponseEntity<?> interServiceCommunication2(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(this.userService.existByUserId(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
