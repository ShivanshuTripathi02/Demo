package com.ai.CarDekho.service;


import com.ai.CarDekho.model.Car;
import com.ai.CarDekho.model.PreferenceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String callAI(PreferenceRequest req, List<Car> cars) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String carsJson = mapper.writeValueAsString(cars);

            String prompt = """
You are an expert car advisor for the Indian market.

Buyer profile:
- Budget: %s lakh
- Primary use: %s
- Top priority: %s
- Fuel preference: %s

Here are the top filtered cars with their details:
%s

Instructions:
- Pick the BEST 3 cars for this buyer from the list above
- Use the pros, cons, and bestFor fields to personalize your reasoning
- Be specific — mention actual specs like mileage, safety rating, boot space
- Keep each reason to 2 sentences max

Return ONLY a valid JSON array, no markdown, no explanation:
[
  {
    "name": "Make Model Variant",
    "price": "X.X lakh",
    "reason": "Why this fits THIS buyer specifically",
    "pros": "Best strength for this buyer",
    "cons": "One honest trade-off to know"
  }
]
""".formatted(
                    req.getBudget(),
                    req.getUsage(),
                    req.getPriority(),
                    req.getFuelType(),
                    carsJson
            );

            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", prompt)
                            ))
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, request, Map.class);

            // Extract response text
            Map candidate = (Map) ((List) response.getBody().get("candidates")).get(0);
            Map content = (Map) candidate.get("content");
            Map part = (Map) ((List) content.get("parts")).get(0);

            String output = part.get("text").toString();

            // 🔥 Clean JSON
            int start = output.indexOf("[");
            int end = output.lastIndexOf("]");
            if (start != -1 && end != -1) {
                output = output.substring(start, end + 1);
            }

            return output;

        } catch (Exception e) {
            System.err.println("AI call failed: " + e.getMessage());
            return "[]";
        }
    }
}

