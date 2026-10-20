package com.tubanco.msusuarios.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDto {

    private String token;
    private String tipo = "Bearer";

    // Constructor customizado para inicializarlo solo pasando el token
    // (el tipo ya queda seteado en "Bearer" por defecto)
    public AuthResponseDto(String token) {
        this.token = token;
    }
}