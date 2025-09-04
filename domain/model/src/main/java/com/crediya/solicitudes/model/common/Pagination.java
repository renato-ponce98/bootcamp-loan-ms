package com.crediya.solicitudes.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Pagination {
    private final int page;
    private final int size;
}
