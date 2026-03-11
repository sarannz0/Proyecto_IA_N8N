package com.ponscio_studio.n8n.insfrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "weather_queries")
public class WeatherEntity {

    @Id
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(name = "id", length = 36, columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String place;
    
    @Column(nullable = false)
    private double temperature;
    
    @Column(nullable = false)
    private String weatherCondition;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String recomendedClothes;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public WeatherEntity() {}

    public WeatherEntity(
        UUID id,
        String place,
        double temperature,
        String weatherCondition,
        String recomendedClothes,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.place = place;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.recomendedClothes = recomendedClothes;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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
