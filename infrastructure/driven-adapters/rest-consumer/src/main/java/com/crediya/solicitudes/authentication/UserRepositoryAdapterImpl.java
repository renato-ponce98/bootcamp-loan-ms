package com.crediya.solicitudes.authentication;

import com.crediya.solicitudes.model.user.gateways.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryAdapterImpl implements UserRepository {

    private final WebClient webClient;

    public UserRepositoryAdapterImpl(WebClient.Builder webClientBuilder,
                                     @Value("${adapters.entry-points.rest-consumer.authentication-service.url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<Boolean> existsById(String userId) {
        return webClient.get()
                .uri("/api/v1/usuarios/documento/{identityDocument}", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(false);
    }
}
