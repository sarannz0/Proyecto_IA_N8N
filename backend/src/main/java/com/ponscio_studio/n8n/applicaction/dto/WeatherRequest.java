package com.ponscio_studio.n8n.applicaction.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record WeatherRequest(
    
    @NotBlank(message = "Place es requerido")
    String place,

    @NotBlank(message = "Latitud es requerido")
    String latitud,

    @NotBlank(message = "Longitud es requerido")
    String longitud,

    @NotBlank(message = "Date es requerido")
    LocalDate date

) {}
