package com.crediya.solicitudes.model.status.gateways;

import com.crediya.solicitudes.model.status.Status;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StatusRepository {
    Flux<Status> findByNameIn(List<String> names);
    Mono<Status> findById(Long id);
    Mono<Status> findByName(String name);
}
