package com.tubanco.msgateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public void validarToken(final String token) {
        // Esto verifica matemáticamente la firma y que la fecha de expiración sea válida.
        // Si el token es falso o expiró, va a lanzar una excepción automáticamente.
        Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token);
    }
}