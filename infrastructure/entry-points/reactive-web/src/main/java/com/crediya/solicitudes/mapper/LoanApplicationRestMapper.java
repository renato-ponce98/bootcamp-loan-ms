package com.crediya.solicitudes.mapper;

import com.crediya.solicitudes.dto.CreateLoanApplicationRequest;
import com.crediya.solicitudes.dto.CreateLoanApplicationResponse;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanApplicationRestMapper {
    @Mapping(target = "userId", source = "authenticatedUserId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statusId", ignore = true)
    @Mapping(target = "applicationDate", ignore = true)
    LoanApplication toDomain(CreateLoanApplicationRequest request, String authenticatedUserId);

    @Mapping(target = "message", constant = "Solicitud de préstamo registrada exitosamente.")
    CreateLoanApplicationResponse toResponse(LoanApplication loanApplication);
}
