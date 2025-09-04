package com.crediya.solicitudes.userdetail;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDetailResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal baseSalary;
}
