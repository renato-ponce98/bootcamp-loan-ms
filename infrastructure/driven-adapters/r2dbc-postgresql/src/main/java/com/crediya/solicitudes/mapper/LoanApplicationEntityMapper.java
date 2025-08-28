package com.crediya.solicitudes.mapper;

import com.crediya.solicitudes.data.LoanApplicationEntity;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanApplicationEntityMapper {
    LoanApplication toDomain(LoanApplicationEntity entity);
    LoanApplicationEntity toEntity(LoanApplication domain);
}
