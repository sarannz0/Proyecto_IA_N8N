package com.ponscio_studio.n8n.insfrastructure.persistence.adapter.in;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ponscio_studio.n8n.applicaction.dto.WeatherRequest;
import com.ponscio_studio.n8n.applicaction.service.WorkFlow;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(path = "/weather")
@RequiredArgsConstructor
public class WeatherController {
    
    private final WorkFlow workFlow;

    @PostMapping("/execute")
    public void postMethodName(@RequestBody WeatherRequest request) {
        new WorkFlow().execute(request);
    }
    

}
