package com.crediya.solicitudes;

import com.crediya.solicitudes.data.LoanApplicationR2dbcRepository;
import com.crediya.solicitudes.mapper.LoanApplicationEntityMapper;
import com.crediya.solicitudes.model.common.Pagination;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Override
    public Flux<LoanApplication> findAllByStatusIdIn(List<Long> statusIds, Pagination pagination) {
        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getSize());

        return repository.findAllByStatusIdIn(statusIds, pageRequest)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<LoanApplication> findAllByUserIdAndStatusName(String userId, String statusName) {
        return repository.findAllByUserIdAndStatusName(userId, statusName)
                .map(mapper::toDomain);
    }
}
