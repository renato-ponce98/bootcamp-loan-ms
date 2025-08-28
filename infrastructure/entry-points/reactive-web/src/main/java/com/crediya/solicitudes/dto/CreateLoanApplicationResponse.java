package com.crediya.solicitudes.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateLoanApplicationResponse {
    private Long id;
    private Long statusId;
    private BigDecimal amount;
    private Integer term;
    private LocalDateTime applicationDate;
    private String message;
}
