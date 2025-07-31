package com.fitnessApp.aiservice.service;

import com.fitnessApp.aiservice.entity.AI_Recommendation;
import com.fitnessApp.aiservice.repository.AI_RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AI_RecommendationService {

    @Autowired
    private AI_RecommendationRepository AI_recommendationRepository;


    public List<AI_Recommendation> getUserRecommendation(String userId) {
        return this.AI_recommendationRepository.findByUserId(userId);
    }

    public AI_Recommendation getActivityRecommendation(String activityId) {
        return this.AI_recommendationRepository.findByActivityId(activityId).orElseThrow(() -> new RuntimeException("NO " +
                "RECOMMENDATION FOUND FOR THIS ACTIVITY:" + activityId));
    }

    public AI_Recommendation saveAI_Recommendation(AI_Recommendation aiRecommendation) {

        return this.AI_recommendationRepository.save(aiRecommendation);
    }
}
