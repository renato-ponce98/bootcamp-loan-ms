package com.crediya.solicitudes.model.loanapplication;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
//import lombok.NoArgsConstructor;


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
