package com.ai.CarDekho.util;


import com.ai.CarDekho.model.Car;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

public class JsonLoader {

    public static List<Car> loadCars() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("cars.json").getInputStream();

            return mapper.readValue(inputStream, new TypeReference<List<Car>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load cars.json", e);
        }
    }
}