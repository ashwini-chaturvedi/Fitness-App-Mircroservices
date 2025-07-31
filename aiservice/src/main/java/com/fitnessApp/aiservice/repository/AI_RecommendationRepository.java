package com.fitnessApp.aiservice.repository;

import com.fitnessApp.aiservice.entity.AI_Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AI_RecommendationRepository extends MongoRepository<AI_Recommendation, String> {
    List<AI_Recommendation> findByUserId(String userId);

    Optional<AI_Recommendation> findByActivityId(String activityId);
}
