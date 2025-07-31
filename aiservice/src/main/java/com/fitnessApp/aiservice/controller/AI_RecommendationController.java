package com.fitnessApp.aiservice.controller;

import com.fitnessApp.aiservice.entity.AI_Recommendation;
import com.fitnessApp.aiservice.service.AI_RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class AI_RecommendationController {

    @Autowired
    private AI_RecommendationService recommendationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AI_Recommendation>> getUserRecommendations(@PathVariable String userId) {
        return ResponseEntity.ok(this.recommendationService.getUserRecommendation(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<AI_Recommendation> getActivityRecommendations(@PathVariable String activityId) {
        return ResponseEntity.ok(this.recommendationService.getActivityRecommendation(activityId));
    }

}
