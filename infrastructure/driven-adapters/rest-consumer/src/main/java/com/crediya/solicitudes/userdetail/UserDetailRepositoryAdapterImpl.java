package com.crediya.solicitudes.userdetail;

import com.crediya.solicitudes.model.userdetail.UserDetail;
import com.crediya.solicitudes.model.userdetail.gateways.UserDetailRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import static com.crediya.solicitudes.model.security.SecurityContextKey.TOKEN_CONTEXT_KEY;

@Slf4j
@Repository
@AllArgsConstructor
public class UserDetailRepositoryAdapterImpl implements UserDetailRepository {

    private final WebClient client;
    private final UserDetailMapper mapper;


    @Override
    public Mono<UserDetail> findByIdentityDocument(String identityDocument) {
        return Mono.deferContextual(contextView ->
                getTokenFromContext(contextView)
                        .flatMap(token ->
                                client.get()
                                        .uri("/api/v1/usuarios/detalles/{id}", identityDocument)
                                        .headers(h -> h.setBearerAuth(token))
                                        .retrieve()
                                        .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                                                response -> {
                                                    log.error("Error al obtener detalles del usuario {}. Status: {}", identityDocument, response.statusCode());
                                                    return response.bodyToMono(String.class)
                                                            .flatMap(body -> Mono.error(new RuntimeException("Error del servicio de autenticación: " + body)));
                                                })
                                        .bodyToMono(UserDetailResponseDTO.class)
                                        .map(mapper::toDomain)
                        )
                        .doOnError(err -> log.error("Fallo la llamada al servicio de autenticación para el documento {}", identityDocument, err))
                        .onErrorResume(err -> Mono.empty())
        );
    }

    private Mono<String> getTokenFromContext(ContextView contextView) {
        if (contextView.hasKey(TOKEN_CONTEXT_KEY)) {
            return Mono.just(contextView.get(TOKEN_CONTEXT_KEY));
        } else {
            log.warn("No se encontró el token JWT en el contexto para la llamada entre servicios.");
            return Mono.empty();
        }
    }
}
