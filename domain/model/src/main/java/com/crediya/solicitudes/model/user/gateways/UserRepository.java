package com.crediya.solicitudes.model.user.gateways;

import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<Boolean> existsById(String userId);
}
