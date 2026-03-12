package com.ponscio_studio.n8n.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Weather {
    private UUID id;
    private String place;
    private String temperature;
    private String weatherCondition;
    private Object recommendedClothes;
    private LocalDateTime createdAt;

    public Weather() {}
    
    public Weather(
        String place,
        String temperature,
        String weatherCondition,
        Object recommendedClothes,
        LocalDateTime createdAt
    ) {
        this.id = UUID.randomUUID();
        this.place = place;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.recommendedClothes = recommendedClothes;
        this.createdAt = createdAt;
    }
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getpPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public Object getRecomendedClothes() {
        return recommendedClothes;
    }

    public void setRecomendedClothes(Object recomendedClothes) {
        this.recommendedClothes = recomendedClothes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    

}
