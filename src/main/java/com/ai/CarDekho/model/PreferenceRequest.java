package com.ai.CarDekho.model;

import lombok.Data;

@Data
public class PreferenceRequest {
    private double budget; // in lakhs
    private String fuelType;
    private String usage;
    private String priority;
}