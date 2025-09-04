package com.crediya.solicitudes.api;

import com.crediya.solicitudes.dto.CreateLoanApplicationRequest;
import com.crediya.solicitudes.dto.CreateLoanApplicationResponse;
import com.crediya.solicitudes.dto.ErrorResponse;
import com.crediya.solicitudes.mapper.LoanApplicationRestMapper;
import com.crediya.solicitudes.model.common.Pagination;
import com.crediya.solicitudes.model.loanapplication.LoanApplicationDetail;
import com.crediya.solicitudes.usecase.createloanapplication.CreateLoanApplicationUseCase;
import com.crediya.solicitudes.usecase.getloanapplicationsforreview.GetLoanApplicationsForReviewUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Loan Application Management", description = "Endpoints para la gestión de solicitudes de préstamo")
public class LoanApplicationController {

    private final CreateLoanApplicationUseCase createLoanApplicationUseCase;
    private final GetLoanApplicationsForReviewUseCase getLoanApplicationsForReviewUseCase;
    private final LoanApplicationRestMapper loanApplicationRestMapper;

    @PostMapping(path = "/solicitud")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENTE')")
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
            @Valid @RequestBody CreateLoanApplicationRequest request, Authentication authentication) {
        String authenticatedUserId = authentication.getName();
        log.info("Iniciando registro de solicitud de préstamo para el usuario: {}", authenticatedUserId);

        return Mono.just(request)
                .map(req -> loanApplicationRestMapper.toDomain(req, authenticatedUserId))
                .flatMap(application -> createLoanApplicationUseCase.createNewLoanApplication(application,authenticatedUserId))
                .map(loanApplicationRestMapper::toResponse)
                .doOnSuccess(response -> log.info("Solicitud registrada con ID: {}", response.getId()))
                .doOnError(error -> log.error("Error al registrar solicitud para el usuario {}: {}", authenticatedUserId, error.getMessage()));
    }

    @GetMapping(path = "/solicitud")
    @PreAuthorize("hasRole('ASESOR')")
    @Operation(
            summary = "Listar solicitudes para revisión manual",
            description = "Obtiene una lista paginada y filtrable de solicitudes que requieren revisión por parte de un asesor.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de solicitudes obtenida exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoanApplicationDetail.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Acceso denegado. Se requiere rol de ASESOR."
                    )
            }
    )
    public Mono<ResponseEntity<List<LoanApplicationDetail>>> getApplicationsForReview(
            @Parameter(description = "Lista de nombres de estados a filtrar (ej. PENDIENTE_REVISION,RECHAZADO)")
            @RequestParam(value = "statuses", defaultValue = "PENDIENTE_REVISION,RECHAZADO") List<String> statuses,

            @Parameter(description = "Número de la página a obtener (empezando en 0)")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @Parameter(description = "Tamaño de la página (cantidad de resultados por página)")
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        log.info("Iniciando búsqueda de solicitudes para revisión. Filtros: statuses={}, page={}, size={}", statuses, page, size);
        Pagination pagination = new Pagination(page, size);

        return getLoanApplicationsForReviewUseCase.getApplications(statuses, pagination)
                .collectList()
                .map(list -> ResponseEntity.ok().body(list))
                .doOnSuccess(response -> log.info("Búsqueda de solicitudes completada. Se encontraron {} resultados.", response.getBody() != null ? response.getBody().size() : 0))
                .doOnError(error -> log.error("Error al buscar solicitudes para revisión", error));
    }
}
