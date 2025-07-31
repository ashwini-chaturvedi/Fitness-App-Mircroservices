package com.fitnessApp.api_gateway.filters;

import com.fitnessApp.api_gateway.User.dto.RegisterRequest;
import com.fitnessApp.api_gateway.User.service.UserCommunicationService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class KeycloakUserSyncFilter implements WebFilter {

    @Autowired
    private UserCommunicationService userCommunicationService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain filterChain) {
        String userId = exchange
                .getRequest()
                .getHeaders()
                .getFirst("X-User-ID");
        String token = exchange
                .getRequest()
                .getHeaders()
                .getFirst("Authorization");

        //we will extract the values from the token and make a RegisterRequest type object of it
        // to send it to the User-Service to register it as new User
        RegisterRequest registerRequest = getUserDetails(token);

        if (userId == null) {
            userId = registerRequest.getKeycloakId();
        }
        if (userId != null && token != null) {
            String finalUserId = userId;
            return userCommunicationService.validateUser(userId)
                    .flatMap(exist -> {
                        if (!exist) {
                            //if the user does not exist we would register the new user
                            if (registerRequest != null) {
                                return userCommunicationService
                                        .registerNewUser(registerRequest)
                                        .then(Mono.empty());
                            } else {
                                return Mono.empty();
                            }

                        } else {
                            log.info("user already present---");
                            return Mono.empty();
                        }
                    })
                    .then(Mono.defer(() -> {
                        ServerHttpRequest mutatedRequest = exchange.getRequest()
                                .mutate()
                                .header("X-USER-ID", finalUserId)
                                .build();

                        return filterChain.filter(exchange.mutate().request(mutatedRequest).build());
                    }));
        }
        return filterChain.filter(exchange);//if userId or filter is not present there we will simply continue the
        // filter chain
    }

    private RegisterRequest getUserDetails(String token) {
        try {
            String tokenWithoutBearer = token.replace("Bearer ", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            RegisterRequest registerRequest = new RegisterRequest();

            registerRequest.setEmailId(claims.getStringClaim("email"));
            registerRequest.setKeycloakId(claims.getStringClaim("sub"));
            registerRequest.setPassword(claims.getStringClaim("preferred_username") + "@123");
            registerRequest.setFirstName(claims.getStringClaim("given_name"));
            registerRequest.setLastName(claims.getStringClaim("family_name"));

            return registerRequest;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }
}
