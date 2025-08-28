package com.crediya.solicitudes.api;

import com.crediya.solicitudes.dto.ErrorResponse;
import com.crediya.solicitudes.usecase.exceptions.InvalidLoanTypeException;
import com.crediya.solicitudes.usecase.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidLoanTypeException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(RuntimeException ex, ServerWebExchange exchange) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(WebExchangeBindException ex, ServerWebExchange exchange) {
        List<String> details = ex.getBindingResult()
                .getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message("Error de validación. Por favor, revise los datos enviados.")
                .path(exchange.getRequest().getPath().value())
                .details(details)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
