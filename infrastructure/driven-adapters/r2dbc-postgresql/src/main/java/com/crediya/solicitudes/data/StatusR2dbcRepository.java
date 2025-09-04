package com.crediya.solicitudes.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusR2dbcRepository extends ReactiveCrudRepository<StatusEntity, Long> {
    Flux<StatusEntity> findByNameIn(List<String> names);
}
