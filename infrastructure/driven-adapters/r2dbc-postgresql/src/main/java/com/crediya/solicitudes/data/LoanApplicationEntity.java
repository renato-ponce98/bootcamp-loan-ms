package com.crediya.solicitudes.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("loan_applications")
public class LoanApplicationEntity {
    @Id
    private Long id;
    @Column("user_id")
    private String userId;
    @Column("loan_type_id")
    private Long loanTypeId;
    @Column("status_id")
    private Long statusId;
    private BigDecimal amount;
    private Integer term;
    @Column("application_date")
    private LocalDateTime applicationDate;
}
