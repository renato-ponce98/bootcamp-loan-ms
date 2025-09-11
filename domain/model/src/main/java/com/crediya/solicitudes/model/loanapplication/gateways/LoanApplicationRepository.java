package com.crediya.solicitudes.model.loanapplication.gateways;

import com.crediya.solicitudes.model.common.Pagination;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface LoanApplicationRepository {
    Mono<LoanApplication> save(LoanApplication loanApplication);
    Flux<LoanApplication> findAllByStatusIdIn(List<Long> statusIds, Pagination pageable);
    Flux<LoanApplication> findAllByUserIdAndStatusName(String userId, String statusName);
    Mono<LoanApplication> findById(Long id);
}
