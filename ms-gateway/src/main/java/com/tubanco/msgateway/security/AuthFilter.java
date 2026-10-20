package com.tubanco.msgateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

            // 1. Verificamos si la petición trae el header "Authorization"
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete(); // Cortamos el paso acá
            }

            // 2. Extraemos el texto del header
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            // 3. Limpiamos la palabra "Bearer " para quedarnos solo con el token largo
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }

            // 4. Intentamos validar el token
            try {
                jwtUtil.validarToken(authHeader);
            } catch (Exception e) {
                // Si la firma es inválida, el token fue modificado o ya venció
                System.out.println("❌ ERROR VALIDANDO TOKEN: " + e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // 5. Si todo está perfecto, dejamos que la petición siga su camino al microservicio
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Clase vacía necesaria para que Spring Cloud Gateway instancie el filtro
    }
}