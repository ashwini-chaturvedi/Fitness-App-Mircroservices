package com.fitnessApp.activityService.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@AllArgsConstructor
public class UserValidationService {

    private final WebClient userServiceWebClient;

    public boolean validateUser(String keycloakId) {

        try {
            return Boolean.TRUE.equals(this.userServiceWebClient.get()
                    .uri("/user/{keycloakId}/keycloak/validate", keycloakId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("User Not Found with userId:" + keycloakId);
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new RuntimeException("INVALID REQUEST");
            }

            return false;
        }


    }
}
