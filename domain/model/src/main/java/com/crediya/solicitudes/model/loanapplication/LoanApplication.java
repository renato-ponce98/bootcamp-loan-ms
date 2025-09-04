package com.crediya.solicitudes.model.loanapplication;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder(toBuilder = true)
public class LoanApplication {
    private Long id;
    private String userId;
    private Long loanTypeId;
    private Long statusId;
    private BigDecimal amount;
    private Integer term;
    private LocalDateTime applicationDate;
}
