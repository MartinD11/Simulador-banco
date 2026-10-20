package com.tubanco.msusuarios.service;

import com.tubanco.msusuarios.dto.request.AuthRequestDto;
import com.tubanco.msusuarios.dto.request.UsuarioRequestDto;
import com.tubanco.msusuarios.dto.response.UsuarioResponseDto;

public interface AuthService {

    UsuarioResponseDto registrar(UsuarioRequestDto dto);
    String login(AuthRequestDto dto);
}
