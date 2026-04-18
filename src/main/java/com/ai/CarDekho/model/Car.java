package com.ai.CarDekho.model;


import lombok.Data;
import java.util.List;

@Data
public class Car {
    private int id;
    private String make;
    private String model;
    private String variant;
    private String type;
    private double priceInLakhs;
    private String fuelType;
    private String transmission;
    private int engineCC;
    private Double mileageKmpl;
    private Integer rangeKm;
    private int seatingCapacity;
    private int bootSpaceLiters;
    private int safetyRatingStars;

    private List<String> features;
    private List<String> pros;
    private List<String> cons;
    private List<String> bestFor;
}


//import lombok.Data;
//import java.util.List;
//
//@Data
//public class Car {
//    private int id;
//    private String make;
//    private String model;
//    private String variant;
//    private String type;
//    private double priceInLakhs;
//    private String fuelType;
//    private String transmission;
//    private int engineCC;
//    private Double mileageKmpl;
//    private Integer rangeKm;
//    private int seatingCapacity;
//    private int bootSpaceLiters;
//    private int safetyRatingStars;
//
//    private List<String> features;
//    private List<String> pros;
//    private List<String> cons;
//    private List<String> bestFor;
//}