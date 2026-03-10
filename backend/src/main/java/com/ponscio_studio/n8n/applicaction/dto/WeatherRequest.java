package com.ponscio_studio.n8n.applicaction.dto;

import jakarta.validation.constraints.NotBlank;

public record WeatherRequest(
    @NotBlank(message = "Latitud es requerido")
    String latitud,

    @NotBlank(message = "Longitud es requerido")
    String longitud
) {}
