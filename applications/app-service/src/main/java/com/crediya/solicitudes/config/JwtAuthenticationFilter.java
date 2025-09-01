package com.crediya.solicitudes.config;

import com.crediya.solicitudes.model.security.gateways.TokenValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final TokenValidator tokenValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = resolveToken(exchange);
        if (token == null) {
            return chain.filter(exchange);
        }

        return tokenValidator.validateToken(token)
                .flatMap(authenticatedUser -> {
                    log.info("Token validado. Usuario extraído: userId={}, role={}",
                            authenticatedUser.getUserId(), authenticatedUser.getRole());
                    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(authenticatedUser.getRole()));
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(authenticatedUser.getUserId(), null, authorities);
                    return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                })
                .switchIfEmpty(chain.filter(exchange));
    }
    private String resolveToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
