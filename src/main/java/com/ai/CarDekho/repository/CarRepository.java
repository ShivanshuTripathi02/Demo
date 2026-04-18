package com.ai.CarDekho.repository;


import com.ai.CarDekho.model.Car;
import com.ai.CarDekho.util.JsonLoader;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarRepository {

    private final List<Car> cars = JsonLoader.loadCars();

    public List<Car> getAllCars() {
        return cars;
    }
}