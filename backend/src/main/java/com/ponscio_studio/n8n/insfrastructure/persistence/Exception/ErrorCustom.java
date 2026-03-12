package com.ponscio_studio.n8n.insfrastructure.persistence.Exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorCustom(
    LocalDateTime date,
    Map<String, String> errors,
    String message,
    int status
) {}
