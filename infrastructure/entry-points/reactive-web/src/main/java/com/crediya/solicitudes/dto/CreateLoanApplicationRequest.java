package com.crediya.solicitudes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoanApplicationRequest {

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
