package com.ai.CarDekho.service;

import com.ai.CarDekho.model.Car;
import com.ai.CarDekho.model.PreferenceRequest;
import com.ai.CarDekho.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final CarRepository repository;

    public RecommendationService(CarRepository repository) {
        this.repository = repository;
    }

    public List<Car> recommend(PreferenceRequest req) {

        return repository.getAllCars().stream()

                // Budget filter
                .filter(car -> car.getPriceInLakhs() <= req.getBudget())

                // Fuel filter
                .filter(car -> req.getFuelType().equalsIgnoreCase("any") ||
                        car.getFuelType().equalsIgnoreCase(req.getFuelType()))

                // Basic ranking
                .sorted((a, b) -> score(b, req) - score(a, req))

                .limit(3)
                .toList();
    }

    private int score(Car car, PreferenceRequest req) {
        int score = 0;

        switch (req.getPriority().toLowerCase()) {
            case "mileage" -> {
                if (car.getMileageKmpl() != null)
                    score += car.getMileageKmpl();
            }
            case "safety" -> score += car.getSafetyRatingStars() * 10;
            case "features" -> score += car.getFeatures().size() * 5;
        }

        // Bonus for matching use-case
        if (car.getBestFor() != null &&
                car.getBestFor().toString().toLowerCase().contains(req.getUsage().toLowerCase())) {
            score += 20;
        }

        return score;
    }
}
