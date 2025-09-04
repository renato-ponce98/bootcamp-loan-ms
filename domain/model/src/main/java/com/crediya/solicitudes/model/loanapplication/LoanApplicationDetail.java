package com.crediya.solicitudes.model.loanapplication;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class LoanApplicationDetail {
    private Long id;
    private BigDecimal amount;
    private Integer term;
    private LocalDateTime applicationDate;

    private String userFullName;
    private String userEmail;
    private BigDecimal userBaseSalary;
    private String loanTypeName;
    private BigDecimal loanInterestRate;
    private String statusName;
    private BigDecimal totalMonthlyDebt;
}
