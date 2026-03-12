package com.ponscio_studio.n8n.applicaction.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record WeatherRequest(
    
    @NotNull(message = "Place es requerido")
    String place,

    @NotNull(message = "Latitud es requerido")
    String latitud,

    @NotNull(message = "Longitud es requerido")
    String longitud,

    @NotNull(message = "Date es requerido")
    LocalDate date

) {}
