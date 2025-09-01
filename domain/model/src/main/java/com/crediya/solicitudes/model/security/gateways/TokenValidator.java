package com.crediya.solicitudes.model.security.gateways;

import com.crediya.solicitudes.model.security.AuthenticatedUser;
import reactor.core.publisher.Mono;

public interface TokenValidator {
    Mono<AuthenticatedUser> validateToken(String token);
}
