package com.ponscio_studio.n8n.applicaction.mapper;

import org.springframework.stereotype.Component;

import com.ponscio_studio.n8n.domain.model.Weather;
import com.ponscio_studio.n8n.insfrastructure.persistence.entity.WeatherEntity;

@Component
public class WeatherMapper {
    
    public Weather toDomain(WeatherEntity entity) {
        Weather newWeather = new Weather();

        newWeather.setId(entity.getId());
        newWeather.setPlace(entity.getPlace());
        newWeather.setTemperature(entity.getTemperature());
        newWeather.setWeatherCondition(entity.getWeatherCondition());
        newWeather.setRecomendedClothes(entity.getRecomendedClothes());
        newWeather.setCreatedAt(entity.getCreatedAt());

        return newWeather;
    }

    public WeatherEntity toEntity(Weather domain) {
        WeatherEntity newWeather = new WeatherEntity();
        
        newWeather.setId(domain.getId());
        newWeather.setPlace(domain.getpPlace());
        newWeather.setTemperature(domain.getTemperature());
        newWeather.setWeatherCondition(domain.getWeatherCondition());
        newWeather.setRecomendedClothes(domain.getRecomendedClothes().toString());
        newWeather.setCreatedAt(domain.getCreatedAt());

        return newWeather;
    }

}
