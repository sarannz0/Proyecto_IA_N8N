package com.ponscio_studio.n8n.insfrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.ponscio_studio.n8n.insfrastructure.persistence.entity.WeatherEntity;

@Component
public interface WeatherJpaRepository extends JpaRepository<WeatherEntity, UUID>{   
}
