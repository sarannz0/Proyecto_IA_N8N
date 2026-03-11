package com.ponscio_studio.n8n.insfrastructure.persistence.adapter.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ponscio_studio.n8n.applicaction.mapper.WeatherMapper;
import com.ponscio_studio.n8n.domain.model.Weather;
import com.ponscio_studio.n8n.domain.port.WeatherRepository;
import com.ponscio_studio.n8n.insfrastructure.persistence.repository.WeatherJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WeatherRepositoryAdapter implements WeatherRepository {

    private final WeatherJpaRepository repository;
    private final WeatherMapper weatherMapper;

    @Override
    public List<Weather> findAll() {
        return repository.findAll().stream().map(weatherMapper::toDomain).toList();
    }

    @Override
    public Optional<Weather> findById(UUID id) {
        return repository.findById(id).map(weatherMapper::toDomain);
    }

    @Override
    public Weather save(Weather weather) {
        return weatherMapper.toDomain(repository.save(weatherMapper.toEntity(weather)));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
    
}
