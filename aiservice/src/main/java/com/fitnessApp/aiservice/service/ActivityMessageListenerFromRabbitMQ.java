package com.fitnessApp.aiservice.service;

import com.fitnessApp.aiservice.entity.AI_Recommendation;
import com.fitnessApp.aiservice.entity.Activity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActivityMessageListenerFromRabbitMQ {

    @Autowired
    private ActivityAIService aiService;

    @Autowired
    private AI_RecommendationService aiRecommendationService;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activityFromQueue) {

        log.info("Received Activity:{}", activityFromQueue);

        AI_Recommendation aiRecommendation = aiService.generateRecommendation(activityFromQueue);

        this.aiRecommendationService.saveAI_Recommendation(aiRecommendation);
    }
}
