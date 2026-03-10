package com.ponscio_studio.n8n.applicaction.service;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ponscio_studio.n8n.applicaction.dto.WeatherRequest;

@Component
public class WorkFlow {
    
    public void execute(WeatherRequest data) {
        final String URL = "http://localhost:5678/webhook-test/335be737-5baa-48db-b441-2a26aba8c98b";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<WeatherRequest> request = new HttpEntity<>(data);
        
        try {restTemplate.postForEntity(URL, request, String.class); }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se pudo conectar con N8N");
        }
        
    }

}
