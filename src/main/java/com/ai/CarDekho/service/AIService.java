package com.ai.CarDekho.service;


import com.ai.CarDekho.model.Car;
import com.ai.CarDekho.model.PreferenceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String callAI(PreferenceRequest req, List<Car> cars) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String carsJson = mapper.writeValueAsString(cars);

            String prompt = """
You are an expert automobile advisor for the Indian market.

User Preferences:
Budget: %s lakh
Usage: %s
Priority: %s
Fuel: %s

Cars:
%s

Recommend top 3 cars with:
- reason
- pros
- cons

Return JSON only.
"""
                    .formatted(
                            req.getBudget(),
                            req.getUsage(),
                            req.getPriority(),
                            req.getFuelType(),
                            carsJson
                    );

            String url = "http://localhost:11434/api/generate";

            Map<String, Object> body = Map.of(
                    "model", "llama3",
                    "prompt", prompt,
                    "stream", false
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, request, Map.class);

            return response.getBody().get("response").toString();

        } catch (Exception e) {
            return "AI failed: " + e.getMessage();
        }
    }
}



//import com.ai.CarDekho.model.Car;
//import com.ai.CarDekho.model.PreferenceRequest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class AIService {
//
//    @Value("${openai.api.key}")
//    private String apiKey;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public String callAI(PreferenceRequest req, List<Car> cars) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            String carsJson = mapper.writeValueAsString(cars);
//
//            String prompt = """
//You are an expert automobile advisor for India.
//
//User:
//Budget: %s lakh
//Usage: %s
//Priority: %s
//Fuel: %s
//
//Cars:
//%s
//
//Recommend top 3 cars with reason, pros, cons.
//Return JSON only.
//"""
//                    .formatted(req.getBudget(), req.getUsage(), req.getPriority(), req.getFuelType(), carsJson);
//
//            String url = "https://api.openai.com/v1/chat/completions";
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.setBearerAuth(apiKey);
//
//            Map<String, Object> body = Map.of(
//                    "model", "gpt-4o-mini",
//                    "messages", List.of(
//                            Map.of("role", "user", "content", prompt)
//                    )
//            );
//
//            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
//
//            // Extract AI text
//            Map choice = (Map) ((List) response.getBody().get("choices")).get(0);
//            Map message = (Map) choice.get("message");
//
//            return message.get("content").toString();
//
//        } catch (Exception e) {
//            return "AI failed: " + e.getMessage();
//        }
//    }
//}



//import com.ai.CarDekho.model.Car;
//import com.ai.CarDekho.model.PreferenceRequest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class AIService {
//
//    public String buildPrompt(PreferenceRequest req, List<Car> cars) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            String carsJson = mapper.writeValueAsString(cars);
//
//            return """
//You are an expert automobile advisor helping Indian car buyers.
//
//User Preferences:
//- Budget: ₹%s lakh
//- Usage: %s
//- Priority: %s
//- Fuel Type: %s
//
//Cars:
//%s
//
//Recommend top 3 cars with:
//- reason
//- 1 pro
//- 1 con
//
//Keep it simple and practical.
//Consider Indian road conditions and maintenance.
//
//Return JSON.
//"""
//                    .formatted(
//                            req.getBudget(),
//                            req.getUsage(),
//                            req.getPriority(),
//                            req.getFuelType(),
//                            carsJson
//                    );
//
//        } catch (Exception e) {
//            return "Error building AI prompt";
//        }
//    }
//}




//import com.ai.CarDekho.model.BuyerPreferences;
//import com.ai.CarDekho.model.Car;
//import com.ai.CarDekho.model.PreferenceRequest;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestClient;
//import tools.jackson.databind.ObjectMapper;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class AIService {
//
//    @Value("${anthropic.api.key}")
//    private String apiKey;
//
//    private final RestClient restClient = RestClient.create();
//
//    public List<PreferenceRequest> getRecommendations(
//            BuyerPreferences prefs, List<Car> cars) {
//
//        String prompt = buildPrompt(prefs, cars);
//
//        // Call Anthropic API
//        String response = restClient.post()
//                .uri("https://api.anthropic.com/v1/messages")
//                .header("x-api-key", apiKey)
//                .header("anthropic-version", "2023-06-01")
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Map.of(
//                        "model", "claude-sonnet-4-20250514",
//                        "max_tokens", 1000,
//                        "messages", List.of(
//                                Map.of("role", "user", "content", prompt)
//                        )
//                ))
//                .retrieve()
//                .body(String.class);
//
//        return parseRecommendations(response);
//    }
//
//    private String buildPrompt(BuyerPreferences prefs, List<Car> cars) {
//        return """
//            You are a car buying advisor for the Indian market.
//            Here is the car dataset: %s
//
//            The buyer has told you:
//            - Budget: %s
//            - Primary use: %s
//            - Family size: %s
//            - Priorities: %s
//            - Nice to have: %s
//
//            Return exactly 3 car recommendations ranked by fit.
//            Respond in JSON only, no markdown, with this structure:
//            [{"carName": "...", "variant": "...", "price": "...",
//              "whyItFits": "...", "tradeOff": "..."}]
//            """.formatted(
//                new ObjectMapper().writeValueAsString(cars),
//                prefs.budgetRange(), prefs.primaryUse(),
//                prefs.familySize(), prefs.priorities(),
//                prefs.niceToHave()
//        );
//    }
//}
