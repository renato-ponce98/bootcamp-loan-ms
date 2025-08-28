package com.crediya.solicitudes.model.loanapplication.gateways;

import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import reactor.core.publisher.Mono;

public interface LoanApplicationRepository {
    Mono<LoanApplication> save(LoanApplication loanApplication);
}
