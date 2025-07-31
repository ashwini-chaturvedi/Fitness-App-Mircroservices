package com.fitnessApp.activityService.controller;

import com.fitnessApp.activityService.dto.ActivityRequest;
import com.fitnessApp.activityService.dto.ActivityResponse;
import com.fitnessApp.activityService.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping("/create")
    public ResponseEntity<?> createActivity(@RequestBody ActivityRequest request) {
        return ResponseEntity.ok(this.activityService.createActivity(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@PathVariable String userId) {
        return ResponseEntity.ok(this.activityService.getUserActivities(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> getActivity(@PathVariable String id) {
        return ResponseEntity.ok(this.activityService.getActivity(id));
    }
}
