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
    @Column("interest_rate")
    private BigDecimal interestRate;
}
