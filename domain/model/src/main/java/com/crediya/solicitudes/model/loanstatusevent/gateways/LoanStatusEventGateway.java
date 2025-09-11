package com.crediya.solicitudes.model.loanstatusevent.gateways;

import com.crediya.solicitudes.model.loanapplication.LoanApplicationDetail;
import reactor.core.publisher.Mono;

public interface LoanStatusEventGateway {
    Mono<Void> publishLoanStatusUpdate(LoanApplicationDetail applicationDetail);
}
