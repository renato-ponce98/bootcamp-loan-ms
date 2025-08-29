package com.crediya.solicitudes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateLoanApplicationRequest {

    @NotBlank(message = "El ID de usuario no puede estar vacío.")
    private String userId;

    @NotNull(message = "El tipo de préstamo no puede ser nulo.")
    @Positive(message = "El ID del tipo de préstamo debe ser un número positivo.")
    private Long loanTypeId;

    @NotNull(message = "El monto no puede ser nulo.")
    @Positive(message = "El monto debe ser un valor positivo.")
    private BigDecimal amount;

    @NotNull(message = "El plazo no puede ser nulo.")
    @Positive(message = "El plazo debe ser un número positivo.")
    private Integer term;
}
