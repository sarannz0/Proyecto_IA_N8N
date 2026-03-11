package com.ponscio_studio.n8n.domain.port;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ponscio_studio.n8n.domain.model.Weather;

public interface WeatherRepository {
    List<Weather> findAll();
    Optional<Weather> findById(UUID id);
    Weather save(Weather weather);
    void deleteById(UUID id);
}
