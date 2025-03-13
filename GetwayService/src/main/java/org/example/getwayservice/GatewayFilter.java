package org.example.getwayservice;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class GatewayFilter extends AbstractGatewayFilterFactory<GatewayFilter.Config> {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    public GatewayFilter() {
        super(Config.class);
    }

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }


    @Override
    public org.springframework.cloud.gateway.filter.GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String string = exchange.getRequest().getPath().toString();
            if (string.contains("/users/register/**")) {
                return chain.filter(exchange);
            }

            String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            try {
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(authorization)
                        .getPayload();
            } catch (JwtException | IllegalArgumentException e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);

        };




    }

    public static class Config{

    }
}
