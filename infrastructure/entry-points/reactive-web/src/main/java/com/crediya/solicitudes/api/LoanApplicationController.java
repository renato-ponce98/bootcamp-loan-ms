package com.crediya.solicitudes.api;

import com.crediya.solicitudes.dto.CreateLoanApplicationRequest;
import com.crediya.solicitudes.dto.CreateLoanApplicationResponse;
import com.crediya.solicitudes.dto.ErrorResponse;
import com.crediya.solicitudes.mapper.LoanApplicationRestMapper;
import com.crediya.solicitudes.usecase.createloanapplication.CreateLoanApplicationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Loan Application Management", description = "Endpoints para la gestión de solicitudes de préstamo")
public class LoanApplicationController {

    private final CreateLoanApplicationUseCase createLoanApplicationUseCase;
    private final LoanApplicationRestMapper loanApplicationRestMapper;

    @PostMapping(path = "/solicitud")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar una nueva solicitud de préstamo",
            description = "Crea una nueva solicitud de préstamo para un usuario existente, validando la información proporcionada.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Solicitud registrada exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateLoanApplicationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos (ej. monto negativo, tipo de préstamo inexistente)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public Mono<CreateLoanApplicationResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la solicitud de préstamo a registrar", required = true, content = @Content(schema = @Schema(implementation = CreateLoanApplicationRequest.class)))
            @Valid @RequestBody CreateLoanApplicationRequest request) {
        log.info("Iniciando registro de solicitud de préstamo para el usuario: {}", request.getUserId());

        return Mono.just(request)
                .map(loanApplicationRestMapper::toDomain)
                .flatMap(createLoanApplicationUseCase::createNewLoanApplication)
                .map(loanApplicationRestMapper::toResponse)
                .doOnSuccess(response -> log.info("Solicitud registrada con ID: {}", response.getId()))
                .doOnError(error -> log.error("Error al registrar solicitud para el usuario {}: {}", request.getUserId(), error.getMessage()));
    }
}
