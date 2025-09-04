package com.crediya.solicitudes.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Table("loan_types")
public class LoanTypeEntity {
    @Id
    private Long id;
    private String name;
    @Column("min_amount")
    private BigDecimal minAmount;
    @Column("max_amount")
    private BigDecimal maxAmount;
    @Column("interest_rate")
    private BigDecimal interestRate;
    @Column("automatic_validation")
    private Boolean automaticValidation;
}
