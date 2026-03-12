package com.ponscio_studio.n8n.applicaction.useCase;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ponscio_studio.n8n.applicaction.dto.WeatherRequest;
import com.ponscio_studio.n8n.applicaction.dto.WeatherRequestFromWorkFlow;
import com.ponscio_studio.n8n.applicaction.service.WorkFlow;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetWeatherUseCase {
    private final SaveWeatherUseCase saveWeatherUseCase;
    private final WorkFlow workFlow;
    
    public WeatherRequestFromWorkFlow execute(WeatherRequest request) {
        ResponseEntity<WeatherRequestFromWorkFlow> response = workFlow.execute(request);
        WeatherRequestFromWorkFlow body = (WeatherRequestFromWorkFlow) response.getBody(); 

        if (body == null) throw new RuntimeException("N8N no retorno nadota");
        saveWeatherUseCase.execute(body);
        
        return body;
    }
}
