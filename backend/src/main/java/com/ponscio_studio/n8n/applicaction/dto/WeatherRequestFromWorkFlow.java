package com.ponscio_studio.n8n.applicaction.dto;


import jakarta.validation.constraints.NotNull;

public record WeatherRequestFromWorkFlow(

    @NotNull(message = "City es requerido")
    String place,

    @NotNull(message = "Temperature es requerido")
    String temperature,

    @NotNull(message = "Weather Condition es requerido")
    String weatherCondition,

    @NotNull(message = "Recomended Clothes es requerido")
    String recommendedClothes,

    @NotNull(message = "Date es requerido")
    String createdAt
    
) {}
