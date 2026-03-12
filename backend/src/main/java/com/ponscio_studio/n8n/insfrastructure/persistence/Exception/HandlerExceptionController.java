package com.ponscio_studio.n8n.insfrastructure.persistence.Exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ponscio_studio.n8n.applicaction.customExceptions.WorkFlowDontStartsException;

@RestControllerAdvice
public class HandlerExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorCustom> validationErrors(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });

        ErrorCustom error = new ErrorCustom(
            LocalDateTime.now(),
            errores,
            ex.getMessage(),
            ex.getStatusCode().value()
        );

        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(WorkFlowDontStartsException.class)
    public ResponseEntity<ErrorCustom> workFlowException(
        WorkFlowDontStartsException ex
    ) {
        ErrorCustom error = new ErrorCustom(
            LocalDateTime.now(),
            null,
            ex.getMessage(),
            500
        );

        return ResponseEntity.internalServerError().body(error);
    }
}
