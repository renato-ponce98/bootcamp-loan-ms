package com.crediya.solicitudes;

import com.crediya.solicitudes.data.LoanTypeR2dbcRepository;
import com.crediya.solicitudes.model.loantype.gateways.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class LoanTypeRepositoryAdapterImpl implements LoanTypeRepository {
    private final LoanTypeR2dbcRepository repository;

    @Override
    public Mono<Boolean> existsById(Long id) {
        return repository.existsById(id);
    }
}
