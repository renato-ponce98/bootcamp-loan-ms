package com.crediya.solicitudes;

import com.crediya.solicitudes.data.LoanApplicationR2dbcRepository;
import com.crediya.solicitudes.mapper.LoanApplicationEntityMapper;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class LoanApplicationRepositoryAdapterImpl implements LoanApplicationRepository {
    private final LoanApplicationR2dbcRepository repository;
    private final LoanApplicationEntityMapper mapper;

    @Override
    public Mono<LoanApplication> save(LoanApplication loanApplication) {
        return Mono.just(loanApplication)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDomain);
    }
}
