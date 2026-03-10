package com.ponscio_studio.n8n.domain.model;

import java.time.LocalDateTime;

public class Weather {
    private int id;
    private String city;
    private double temperature;
    private String weatherCondition;
    private String recomendedClothes;
    private LocalDateTime createdAt;

    
    public Weather() {}
    
    public Weather(
        String city,
        double temperature,
        String weatherCondition,
        String recomendedClothes
    ) {
        this.city = city;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.recomendedClothes = recomendedClothes;
        this.createdAt = LocalDateTime.now();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getRecomendedClothes() {
        return recomendedClothes;
    }

    public void setRecomendedClothes(String recomendedClothes) {
        this.recomendedClothes = recomendedClothes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    

}
