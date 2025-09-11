package com.crediya.solicitudes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {
    @NotBlank(message = "El nuevo estado no puede estar vacío.")
    private String newStatus;
}
