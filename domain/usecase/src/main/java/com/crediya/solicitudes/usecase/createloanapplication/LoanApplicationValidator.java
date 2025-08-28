package com.crediya.solicitudes.usecase.createloanapplication;

import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.usecase.exceptions.DomainValidationException;

import java.math.BigDecimal;

public class LoanApplicationValidator {

    public static void validateForCreation(LoanApplication app) {
        if (app == null) {
            throw new DomainValidationException("La solicitud no puede ser nula.");
        }
        if (isNullOrBlank(app.getUserId())) {
            throw new DomainValidationException("El ID de usuario no puede ser nulo o vacío.");
        }
        if (app.getLoanTypeId() == null || app.getLoanTypeId() <= 0) {
            throw new DomainValidationException("El tipo de préstamo no es válido.");
        }
        if (app.getAmount() == null || app.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainValidationException("El monto debe ser un valor positivo.");
        }
        if (app.getTerm() == null || app.getTerm() <= 0) {
            throw new DomainValidationException("El plazo debe ser un número positivo.");
        }
    }

    private static boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
