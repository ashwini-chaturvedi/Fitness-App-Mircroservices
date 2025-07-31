package com.fitnessApp.activityService.service;

import com.fitnessApp.activityService.dto.ActivityRequest;
import com.fitnessApp.activityService.dto.ActivityResponse;
import com.fitnessApp.activityService.entity.Activity;
import com.fitnessApp.activityService.repository.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserValidationService userValidationService;

    @Autowired
    private RabbitTemplate rabbitTemplate; //this helps to send message to the RabbitMQ

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private ActivityResponse mapActivityToActivityResponse(Activity savedActivity) {
        ActivityResponse activityResponse = new ActivityResponse();

        activityResponse.setId(savedActivity.getId());
        activityResponse.setUserId(savedActivity.getUserId());
        activityResponse.setActivityType(savedActivity.getActivityType());
        activityResponse.setDuration(savedActivity.getDuration());
        activityResponse.setAdditionalMetrics(savedActivity.getAdditionalMetrics());
        activityResponse.setCaloriesBurned(savedActivity.getCaloriesBurned());
        activityResponse.setStartTime(savedActivity.getStartTime());
        activityResponse.setEndTime(savedActivity.getEndTime());
        activityResponse.setCreatedAt(savedActivity.getCreatedAt());
        activityResponse.setUpdatedAt(savedActivity.getUpdatedAt());

        return activityResponse;
    }

    public ActivityResponse createActivity(ActivityRequest request) {
        boolean isValidUser = this.userValidationService.validateUser(request.getKeycloakId());

        if (!isValidUser) {
            throw new RuntimeException("INVALID USER with Keycloak Id:" + request.getKeycloakId());
        }
        Activity activity = new Activity();

        activity.setUserId(request.getKeycloakId());
        activity.setActivityType(request.getActivityType());
        activity.setDuration(request.getDuration());
        activity.setAdditionalMetrics(request.getAdditionalMetrics());
        activity.setCaloriesBurned(request.getCaloriesBurned());
        activity.setStartTime(request.getStartTime());
        activity.setEndTime(request.getEndTime());

        Activity savedActivity = this.activityRepository.save(activity);

        //Once the Data is saved to DB now Publish it to RabbitMQ
        try {
            //this is where we are publishing the data to the Queue on rabbitMQ
            this.rabbitTemplate.convertAndSend(this.exchange, this.routingKey, savedActivity);

        } catch (Exception e) {
            log.error("Failed to Publish Message to RabbitMQ:", e);
            throw new RuntimeException(e);
        }
        return mapActivityToActivityResponse(savedActivity);
    }


    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> existingActivities = this.activityRepository
                .findByUserId(userId);


        return existingActivities.stream()
                .map(this::mapActivityToActivityResponse)
                .collect(Collectors.toList());
    }

    public ActivityResponse getActivity(String id) {
        Activity existingActivity = this.activityRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Activity" +
                        " Not Found with id:" + id));

        return mapActivityToActivityResponse(existingActivity);
    }
}
