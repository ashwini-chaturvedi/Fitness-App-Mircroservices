package com.fitnessApp.api_gateway.User.service;

import com.fitnessApp.api_gateway.User.dto.RegisterRequest;
import com.fitnessApp.api_gateway.User.dto.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserCommunicationService {

    private final WebClient userServiceWebClient;

    public Mono<Boolean> validateUser(String userId) {
        return this.userServiceWebClient.get()
                .uri("/user/{userId}/keycloak/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new RuntimeException("User Not Found with userId:" + userId));
                    } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new RuntimeException("new RuntimeException(\"INVALID REQUEST\")"));
                    }
                    return Mono.error(new RuntimeException("Unexpected Error:"));
                });
    }

    public Mono<UserResponse> registerNewUser(RegisterRequest registerRequest) {

        return this.userServiceWebClient
                .post()
                .uri("/user/register")
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.ALREADY_REPORTED) {
                        return Mono.error(new RuntimeException("User Already Reported with KeycloakId:" + registerRequest.getKeycloakId()));
                    } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new RuntimeException("new RuntimeException(\"INVALID REQUEST\")"));
                    } else if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                        return Mono.error(new RuntimeException("Internal Server Error:" + e.getMessage()));
                    }
                    return Mono.error(new RuntimeException("Unexpected Error:"));
                });
    }
}
