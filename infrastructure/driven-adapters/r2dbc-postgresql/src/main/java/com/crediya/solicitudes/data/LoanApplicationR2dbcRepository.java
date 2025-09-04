package com.crediya.solicitudes.data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface LoanApplicationR2dbcRepository extends ReactiveCrudRepository<LoanApplicationEntity, Long> {
    Flux<LoanApplicationEntity> findAllByStatusIdIn(List<Long> statusIds, PageRequest pageable);

    @Query("SELECT la.* FROM loan_applications la JOIN statuses s ON la.status_id = s.id WHERE la.user_id = :userId AND s.name = :statusName")
    Flux<LoanApplicationEntity> findAllByUserIdAndStatusName(String userId, String statusName);

}
