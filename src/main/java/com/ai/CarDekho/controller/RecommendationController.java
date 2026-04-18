package com.ai.CarDekho.controller;




import com.ai.CarDekho.model.AIResponse;
import com.ai.CarDekho.model.Car;
import com.ai.CarDekho.model.PreferenceRequest;
import com.ai.CarDekho.service.AIService;
import com.ai.CarDekho.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final AIService aiService;

    public RecommendationController(RecommendationService recommendationService,
                                    AIService aiService) {
        this.recommendationService = recommendationService;
        this.aiService = aiService;
    }

    @PostMapping("/recommend")
    public Map<String, Object> recommend(@RequestBody PreferenceRequest request) {

        List<Car> cars = recommendationService.recommend(request);

//        String aiOutput = aiService.callAI(request, cars);
        String aiOutput = aiService.callAI(request, cars);

        Map<String, Object> response = new HashMap<>();
        response.put("recommendations", cars);
        response.put("aiOutput", aiOutput);
        response.put("count", cars.size());

        return response;
    }
}