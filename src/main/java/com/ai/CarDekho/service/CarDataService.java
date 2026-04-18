//package com.ai.CarDekho.service;
//
//import com.ai.CarDekho.model.Car;
//import org.springframework.aot.hint.TypeReference;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Service;
//import tools.jackson.databind.ObjectMapper;
//
//import java.io.IOException;
//import java.util.List;
//
//@Service
//public class CarDataService {
//    private final List<Car> cars;
//
//    public CarDataService(ObjectMapper mapper) throws IOException {
//        Resource resource = new ClassPathResource("cars.json");
//        cars = mapper.readValue(resource.getInputStream(),
//                new TypeReference<List<Car>>() {});
//    }
//
//    public List<Car> getAllCars() { return cars; }
//}