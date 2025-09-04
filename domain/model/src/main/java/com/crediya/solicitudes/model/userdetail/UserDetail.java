package com.crediya.solicitudes.model.userdetail;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
public class UserDetail {
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal baseSalary;
}
