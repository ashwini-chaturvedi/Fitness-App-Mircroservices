package com.fitnessApp.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnessApp.aiservice.entity.AI_Recommendation;
import com.fitnessApp.aiservice.entity.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public AI_Recommendation generateRecommendation(Activity activity) {
        String AI_Prompt = createPromptForActivity(activity);

        String aiResponse = this.geminiService.getAnswerFromAI(AI_Prompt);

        return processingAIResponse(activity, aiResponse);
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                        Analyze this Fitness Activity and provide detailed Recommendations in the Following EXACT JSON Format:
                        {
                             "analysis": {
                                 "overall": "Overall Analysis Here",
                                 "pace": "pace Analysis here",
                                 "heartRate": "Heart rate analysis here",
                                 "caloriesBurned":"Calories Burned here"
                             },
                             "improvements":[
                                 {
                                     "area":"Area Name",
                                     "recommendation":"Detailed recommendation"
                                 }
                             ],
                             "suggestions":[
                                 {
                                     "workout":"workout name",
                                     "description":"Detailed workout description"
                                 }
                             ],
                             "safety":[
                                 "Safety Point 1",
                                 "Safety Point 2"
                             ]
                         }
                        
                        
                        Analyze this Activity:
                        activity type:%s
                        Duration:%d minutes
                        Calories Burned:%d
                        Additional Metrics:%s
                        
                        provide detailed Analysis focusing on the Performance ,improvments,next workout suggestions,& Safety Instructions
                        Ensure the response follows the EXACT JSON format shown above.
                        """,
                activity.getActivityType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }


    private AI_Recommendation processingAIResponse(Activity activity, String aiResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(aiResponse);
            JsonNode textNode = rootNode
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text");

            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n", "")
                    .replaceAll("\\n```", "")
                    .trim();

            //Now we got the AI response in proper Json Format now we will append all the fields of analysis into a
            // string which will be stored as Recommendation in the AI_Recommendation Object
            JsonNode recommendationJSON = objectMapper.readTree(jsonContent);
            JsonNode analysisNode = recommendationJSON.path("analysis");

            StringBuilder fullAnalysisRecomendation = new StringBuilder();
            addAnalysisSection(fullAnalysisRecomendation, analysisNode, "overall", "Overall:");
            addAnalysisSection(fullAnalysisRecomendation, analysisNode, "pace", "Pace/Speed:");
            addAnalysisSection(fullAnalysisRecomendation, analysisNode, "heartRate", "Heart Rate:");
            addAnalysisSection(fullAnalysisRecomendation, analysisNode, "caloriesBurned", "Calories Burned:");

            //Now Mapping Improvements,suggestions etc.
            // these are the data we will be storing in AI_Recommendation Entity

            JsonNode improvementNode = recommendationJSON.path("improvements");
            List<String> improvements = extractImprovements(improvementNode);

            JsonNode suggestionsNode = recommendationJSON.path("suggestions");
            List<String> suggestions = extractSuggestions(suggestionsNode);

            JsonNode safetyNode = recommendationJSON.path("safety");
            List<String> safety = extractSafetyGuidelines(safetyNode);

            //Create an Activity Object and return it
            return AI_Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getActivityType())
                    .recommendation(fullAnalysisRecomendation.toString())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safetyMeasures(safety)
                    .createdAt(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            return createDefaultRecommendation(activity);
        }
    }

    private AI_Recommendation createDefaultRecommendation(Activity activity) {
        return AI_Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getActivityType())
                .recommendation("Unable to Generate Detailed Analysis")
                .improvements(Collections.singletonList("Continue with Your current routine"))
                .suggestions(Collections.singletonList("Consider consulting with Trainer or Fitness Professional"))
                .safetyMeasures(Arrays.asList("Warm Up Before exercise"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safetyList = new ArrayList<>();

        if (safetyNode.isArray()) {
            safetyNode.forEach((safety) -> {

                safetyList.add(String.format("%s", safety.asText()));

            });

        }
        return safetyList.isEmpty() ?
                Collections.singletonList("No Specific Safety Suggestions Provided")
                : safetyList;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();

        if (suggestionsNode.isArray()) {
            suggestionsNode.forEach((suggestion) -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();

                suggestions.add(String.format("%s : %s", workout, description));

            });

        }
        return suggestions.isEmpty() ?
                Collections.singletonList("No Specific Suggestion Provided")
                : suggestions;
    }

    private List<String> extractImprovements(JsonNode improvementNode) {
        List<String> improvements = new ArrayList<>();

        if (improvementNode.isArray()) {
            improvementNode.forEach((improvement) -> {
                String area = improvement.path("area").asText();
                String recommendation = improvement.path("recommendation").asText();

                improvements.add(String.format("%s : %s", area, recommendation));

            });

        }
        return improvements.isEmpty() ?
                Collections.singletonList("No Specific improvements Provided")
                : improvements;

    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if (!analysisNode.path(key).isMissingNode()) {
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n");
        }
    }


}
