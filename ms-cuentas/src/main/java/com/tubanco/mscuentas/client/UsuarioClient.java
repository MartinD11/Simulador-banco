package com.tubanco.mscuentas.client;

import com.tubanco.mscuentas.dto.response.UsuarioResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-usuarios")
public interface UsuarioClient {

    @GetMapping("/api/usuarios/{id}")
    UsuarioResponseDto buscarUsuarioPorId(@PathVariable("id") Long id);
}