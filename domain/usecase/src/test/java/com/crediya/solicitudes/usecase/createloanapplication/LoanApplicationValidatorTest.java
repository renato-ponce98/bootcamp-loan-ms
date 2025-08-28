package com.crediya.solicitudes.usecase.createloanapplication;

import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.usecase.exceptions.DomainValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanApplicationValidatorTest {

    private LoanApplication.LoanApplicationBuilder validAppBuilder;

    @BeforeEach
    void setUp() {
        validAppBuilder = LoanApplication.builder()
                .userId("user-123")
                .loanTypeId(1L)
                .amount(new BigDecimal("10000"))
                .term(12);
    }

    @Test
    @DisplayName("Should not throw exception for a valid application")
    void shouldPassForValidApplication() {
        assertDoesNotThrow(() -> LoanApplicationValidator.validateForCreation(validAppBuilder.build()));
    }

    @Test
    @DisplayName("Should throw exception for a null application")
    void shouldThrowExceptionForNullApplication() {
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> {
            LoanApplicationValidator.validateForCreation(null);
        });
        assertEquals("La solicitud no puede ser nula.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for blank user ID")
    void shouldThrowExceptionForBlankUserId() {
        LoanApplication app = validAppBuilder.userId(" ").build();
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> {
            LoanApplicationValidator.validateForCreation(app);
        });
        assertEquals("El ID de usuario no puede ser nulo o vacío.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for zero amount")
    void shouldThrowExceptionForZeroAmount() {
        LoanApplication app = validAppBuilder.amount(BigDecimal.ZERO).build();
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> {
            LoanApplicationValidator.validateForCreation(app);
        });
        assertEquals("El monto debe ser un valor positivo.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for null loan type ID")
    void shouldThrowExceptionForNullLoanTypeId() {
        LoanApplication app = validAppBuilder.loanTypeId(null).build();
        var exception = assertThrows(DomainValidationException.class, () -> LoanApplicationValidator.validateForCreation(app));
        assertEquals("El tipo de préstamo no es válido.", exception.getMessage());
    }
}
