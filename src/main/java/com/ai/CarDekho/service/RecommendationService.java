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
                .filter(car -> car.getPriceInLakhs() <= req.getBudget())
                .filter(car -> req.getFuelType().equalsIgnoreCase("any") ||
                        car.getFuelType().equalsIgnoreCase(req.getFuelType()))
                .sorted((a, b) -> score(b, req) - score(a, req))
                .limit(5) // send 5 to AI, not 3
                .toList();
    }

    private int score(Car car, PreferenceRequest req) {
        int score = 0;

        switch (req.getPriority().toLowerCase()) {
            case "mileage" -> {
                if (car.getMileageKmpl() != null)
                    score += (int)(car.getMileageKmpl() * 2); // weight it more
            }
            case "safety" -> score += car.getSafetyRatingStars() * 15; // was 10
            case "features" -> score += car.getFeatures().size() * 5;
            case "comfort" -> score += car.getSeatingCapacity() * 5
                    + (car.getBootSpaceLiters() / 50);
        }

        // Bonus: bestFor match
        if (car.getBestFor() != null &&
                car.getBestFor().toString().toLowerCase()
                        .contains(req.getUsage().toLowerCase())) {
            score += 20;
        }

        // Bonus: closer to budget = better value
        double budgetUtilization = car.getPriceInLakhs() / req.getBudget();
        if (budgetUtilization >= 0.75) score += 10; // uses budget well

        // Bonus: EV range if electric
        if (car.getFuelType().equalsIgnoreCase("electric")
                && car.getRangeKm() != null) {
            score += car.getRangeKm() / 10;
        }

        return score;
    }
}
