package com.crediya.solicitudes.usecase.exceptions;

public class InvalidLoanTypeException extends RuntimeException {
    public InvalidLoanTypeException(String message) {
        super(message);
    }
}
