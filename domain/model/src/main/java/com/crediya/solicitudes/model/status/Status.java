package com.crediya.solicitudes.model.status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Status {
    private Long id;
    private String name;
}
