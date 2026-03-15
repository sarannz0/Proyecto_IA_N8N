package com.ponscio_studio.n8n.applicaction.useCase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ponscio_studio.n8n.applicaction.dto.WeatherRequestFromWorkFlow;
import com.ponscio_studio.n8n.domain.model.Weather;
import com.ponscio_studio.n8n.domain.port.WeatherRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveWeatherUseCase {
    private final WeatherRepository weatherRepository;

    @Transactional
    public void execute(WeatherRequestFromWorkFlow request) {
        Weather weather = new Weather(
            request.place(),
            request.temperature(),
            request.weatherCondition(),
            request.recommendedClothes().replace("*", ""),
            LocalDateTime.now()
        );

        weatherRepository.save(weather);
    }
}


