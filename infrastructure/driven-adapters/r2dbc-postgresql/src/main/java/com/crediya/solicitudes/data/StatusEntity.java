package com.crediya.solicitudes.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("statuses")
public class StatusEntity {
    @Id
    private Long id;
    private String name;
}
