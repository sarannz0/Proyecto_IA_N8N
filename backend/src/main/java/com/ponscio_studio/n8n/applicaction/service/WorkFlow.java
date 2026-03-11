package com.ponscio_studio.n8n.applicaction.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ponscio_studio.n8n.applicaction.dto.WeatherRequest;
import com.ponscio_studio.n8n.applicaction.dto.WeatherRequestFromWorkFlow;

@Component
public class WorkFlow {
    
    public ResponseEntity<WeatherRequestFromWorkFlow> execute(WeatherRequest data) {
        final String URL = "http://localhost:5678/webhook-test/335be737-5baa-48db-b441-2a26aba8c98b";
        
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<WeatherRequest> request = new HttpEntity<>(data);
        
        try {
            ResponseEntity<WeatherRequestFromWorkFlow> response = restTemplate.postForEntity(URL, request, WeatherRequestFromWorkFlow.class); 
            return ResponseEntity.ok(response.getBody());

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se pudo conectar con N8N");
        }

    }

}
