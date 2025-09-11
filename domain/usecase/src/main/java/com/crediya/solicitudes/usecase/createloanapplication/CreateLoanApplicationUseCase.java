package com.crediya.solicitudes.usecase.createloanapplication;

import com.crediya.solicitudes.model.autovalidationevent.gateways.AutoValidationEventGateway;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loantype.LoanType;
import com.crediya.solicitudes.model.loantype.gateways.LoanTypeRepository;
import com.crediya.solicitudes.usecase.exceptions.InvalidLoanTypeException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CreateLoanApplicationUseCase {

    private static final Long PENDING_STATUS_ID = 1L;

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final AutoValidationEventGateway autoValidationEventGateway;

    public Mono<LoanApplication> createNewLoanApplication(LoanApplication loanApplication, String authenticatedUserId) {

        loanApplication.setUserId(authenticatedUserId);

        try {
            LoanApplicationValidator.validateForCreation(loanApplication);
        } catch (Exception e) {
            return Mono.error(e);
        }

        return validateLoanType(loanApplication.getLoanTypeId())
                .flatMap(loanType -> {
                    LoanApplication applicationToSave = loanApplication.toBuilder()
                            .statusId(PENDING_STATUS_ID)
                            .applicationDate(LocalDateTime.now())
                            .build();

                    return loanApplicationRepository.save(applicationToSave)
                            .flatMap(savedApplication ->
                                    publishToAutoValidationQueueIfRequired(savedApplication, loanType)
                            );
                });
    }

    private Mono<LoanType> validateLoanType(Long loanTypeId) {
        return loanTypeRepository.findById(loanTypeId)
                .switchIfEmpty(Mono.error(new InvalidLoanTypeException("El tipo de préstamo con ID " + loanTypeId + " no es válido.")));
    }

    private Mono<LoanApplication> publishToAutoValidationQueueIfRequired(LoanApplication savedApplication, LoanType loanType) {
        if (loanType.getAutomaticValidation()) {
            return autoValidationEventGateway.publishForAutoValidation(savedApplication)
                    .thenReturn(savedApplication);
        } else {
            return Mono.just(savedApplication);
        }
    }
}
