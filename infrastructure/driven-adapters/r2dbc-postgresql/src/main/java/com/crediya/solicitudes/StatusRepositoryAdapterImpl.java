package com.crediya.solicitudes;

import com.crediya.solicitudes.data.StatusR2dbcRepository;
import com.crediya.solicitudes.mapper.StatusEntityMapper;
import com.crediya.solicitudes.model.status.Status;
import com.crediya.solicitudes.model.status.gateways.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatusRepositoryAdapterImpl implements StatusRepository {

    private final StatusR2dbcRepository repository;
    private final StatusEntityMapper mapper;

    @Override
    public Flux<Status> findByNameIn(List<String> names) {
        return repository.findByNameIn(names)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Status> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }
}
