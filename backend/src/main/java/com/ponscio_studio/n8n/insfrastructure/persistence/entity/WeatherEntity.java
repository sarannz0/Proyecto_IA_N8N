package com.ponscio_studio.n8n.insfrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "weather_queries")
public class WeatherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private double temperature;
    
    @Column(nullable = false)
    private String weatherCondition;
    
    @Column(nullable = false)
    private String recomendedClothes;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public WeatherEntity() {}

    public WeatherEntity(
        int id,
        String city,
        double temperature,
        String weatherCondition,
        String recomendedClothes,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.city = city;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.recomendedClothes = recomendedClothes;
        this.createdAt = createdAt;
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
