package com.crediya.solicitudes.model.userdetail.gateways;

import com.crediya.solicitudes.model.userdetail.UserDetail;
import reactor.core.publisher.Mono;

public interface UserDetailRepository {
    Mono<UserDetail> findByIdentityDocument(String identityDocument);
}
