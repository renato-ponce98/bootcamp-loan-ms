package com.crediya.solicitudes.config.security;

import com.crediya.solicitudes.model.security.AuthenticatedUser;
import com.crediya.solicitudes.model.security.gateways.TokenValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;

@Component
public class JwtProvider implements TokenValidator {
    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public Mono<Claims> getClaims(String token) {
        return Mono.fromCallable(() -> Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody())
                .onErrorResume(Exception.class, e -> Mono.empty());
    }
    @Override
    public Mono<AuthenticatedUser> validateToken(String token) {
        return getClaims(token)
                .map(claims -> AuthenticatedUser.builder()
                        .userId(claims.get("userId", String.class))
                        .role(claims.get("role", String.class))
                        .build());
    }
}
