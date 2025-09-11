package com.crediya.solicitudes.usecase.updateloanapplicationstatus;

import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loanstatusevent.gateways.LoanStatusEventGateway;
import com.crediya.solicitudes.model.status.gateways.StatusRepository;
import com.crediya.solicitudes.usecase.exceptions.InvalidStatusTransitionException;
import com.crediya.solicitudes.usecase.getloanapplicationsforreview.GetLoanApplicationsForReviewUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateLoanApplicationStatusUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final StatusRepository statusRepository;
    private final LoanStatusEventGateway loanStatusEventGateway;
    private final GetLoanApplicationsForReviewUseCase getLoanApplicationsForReviewUseCase;

    public Mono<LoanApplication> updateStatus(Long applicationId, String newStatusName) {
        return statusRepository.findByName(newStatusName.toUpperCase())
                .switchIfEmpty(Mono.error(new InvalidStatusTransitionException("El estado '" + newStatusName + "' no es válido.")))
                .flatMap(newStatus ->
                        loanApplicationRepository.findById(applicationId)
                                .switchIfEmpty(Mono.error(new InvalidStatusTransitionException("La solicitud con ID " + applicationId + " no existe.")))
                                .flatMap(application -> {
                                    if (application.getStatusId().equals(newStatus.getId())) {
                                        return Mono.error(new InvalidStatusTransitionException("La solicitud ya se encuentra en el estado '" + newStatusName + "'."));
                                    }

                                    application.setStatusId(newStatus.getId());
                                    return loanApplicationRepository.save(application);
                                })
                                .flatMap(this::publishEvent)
                );
    }

    private Mono<LoanApplication> publishEvent(LoanApplication application) {
        return getLoanApplicationsForReviewUseCase.enrichApplicationWithDetails(application)
                .flatMap(loanStatusEventGateway::publishLoanStatusUpdate)
                .thenReturn(application);
    }
}
