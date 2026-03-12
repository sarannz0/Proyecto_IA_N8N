package com.ponscio_studio.n8n.insfrastructure.persistence.adapter.in;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ponscio_studio.n8n.applicaction.dto.WeatherRequest;
import com.ponscio_studio.n8n.applicaction.dto.WeatherRequestFromWorkFlow;
import com.ponscio_studio.n8n.applicaction.useCase.GetWeatherUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final GetWeatherUseCase getWeatherUseCase;

    @PostMapping("/get")
    public ResponseEntity<WeatherRequestFromWorkFlow> postMethodName(@Valid @RequestBody WeatherRequest request) {
        return ResponseEntity.ok(getWeatherUseCase.execute(request));
    }
    
}
