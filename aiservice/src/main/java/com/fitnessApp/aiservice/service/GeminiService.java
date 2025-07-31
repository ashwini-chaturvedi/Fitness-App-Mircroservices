package com.fitnessApp.aiservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {


    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiURL;

    @Value("${gemini.api.key}")
    private String geminiAPIKey;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getAnswerFromAI(String question) {
        //Because the Request should be in a Specific format so we will be using map to map the key and the Objects

        /*
         {
    "contents": [
        {
            "parts": [
                {
                    "text": "Explain how AI works in a few words"
                }
                     ]
        }
                ]
         }
        */

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", question)
                        })
                }
        );

        String response = webClient.post()
                .uri(geminiURL + geminiAPIKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
