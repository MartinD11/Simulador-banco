package com.tubanco.msgateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final JwtUtil jwtUtil;

    public AuthFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            // 1. Verificamos si trae el header Authorization
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }

            try {
                // 2. Validamos la firma criptográfica
                jwtUtil.validarToken(authHeader);

                // 3. Extraemos los datos (Claims) que guardamos en ms-usuarios
                Claims claims = jwtUtil.extraerClaims(authHeader);
                String usuarioId = claims.get("usuarioId").toString();
                String email = claims.getSubject(); // El email suele guardarse en el "sub" (subject)

                // Extraemos el rol, si por algún motivo no viene, por seguridad le asignamos ROLE_USER
                String rol = claims.get("rol") != null ? claims.get("rol").toString() : "ROLE_USER";

                // 4. Clonamos la petición inyectando los datos extraídos como Headers nuevos
                ServerHttpRequest request = exchange.getRequest().mutate()
                        .header("X-Usuario-Id", usuarioId)
                        .header("X-Usuario-Email", email)
                        .header("X-Usuario-Rol", rol)
                        .build();

                // 5. Continuamos el flujo hacia ms-transacciones o ms-cuentas
                return chain.filter(exchange.mutate().request(request).build());

            } catch (Exception e) {
                // Si el token es falso o expiró
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    public static class Config {
    }
}