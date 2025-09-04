package com.crediya.solicitudes.model.loantype.gateways;

import com.crediya.solicitudes.model.loantype.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<Boolean> existsById(Long id);
    Mono<LoanType> findById(Long id);
}
