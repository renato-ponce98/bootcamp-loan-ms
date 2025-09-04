package com.crediya.solicitudes.mapper;

import com.crediya.solicitudes.data.StatusEntity;
import com.crediya.solicitudes.model.status.Status;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatusEntityMapper {
    Status toDomain(StatusEntity entity);
}
