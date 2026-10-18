package com.tubanco.msusuarios.service;

import com.tubanco.msusuarios.dto.request.UsuarioRequestDto;
import com.tubanco.msusuarios.dto.response.UsuarioResponseDto;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDto crearUsuario(UsuarioRequestDto usuario);
    List<UsuarioResponseDto> findAll();
    UsuarioResponseDto findById(Long id);
    UsuarioResponseDto findByDni(String dni);
    UsuarioResponseDto actualizarUsuario(Long id, UsuarioRequestDto usuario);
    void eliminarUsuario(Long id);
}
