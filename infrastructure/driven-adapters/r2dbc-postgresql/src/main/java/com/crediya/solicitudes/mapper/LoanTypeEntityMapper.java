package com.crediya.solicitudes.mapper;

import com.crediya.solicitudes.data.LoanTypeEntity;
import com.crediya.solicitudes.model.loantype.LoanType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanTypeEntityMapper {
    LoanType toDomain(LoanTypeEntity entity);
}
