package com.tubanco.msusuarios.service.impl;

import com.tubanco.bancocommon.exceptions.RecursoNoEncontradoException;
import com.tubanco.msusuarios.dto.request.UsuarioRequestDto;
import com.tubanco.msusuarios.dto.response.UsuarioResponseDto;
import com.tubanco.msusuarios.entity.Usuario;
import com.tubanco.msusuarios.enums.Estado;
import com.tubanco.msusuarios.mapper.UsuarioMapper;
import com.tubanco.msusuarios.repository.UsuarioRepository;
import com.tubanco.msusuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

     private final  UsuarioRepository usuarioRepository;
     private final UsuarioMapper usuarioMapper;


    @Override
    @Transactional
    public UsuarioResponseDto crearUsuario(UsuarioRequestDto usuario) {
        if(usuarioRepository.findByDni(usuario.getDni()).isPresent()) {
            throw new IllegalArgumentException("El usuario con DNI " + usuario.getDni() + " ya existe.");
        }

        Usuario usuarioEntity = usuarioMapper.toEntity(usuario);
        Usuario usuarioGuardado = usuarioRepository.save(usuarioEntity);

        return usuarioMapper.toResponseDto(usuarioGuardado);

    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDto findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(()-> new RecursoNoEncontradoException("El usuario con ID " + id + " no fue encontrado."));

        return usuarioMapper.toResponseDto(usuario);

    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDto findByDni(String dni) {
        Usuario usuario = usuarioRepository.findByDni(dni)
                .orElseThrow(()-> new RecursoNoEncontradoException("El usuario con DNI " + dni + " no fue encontrado."));

        return usuarioMapper.toResponseDto(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDto actualizarUsuario(Long id, UsuarioRequestDto usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario con ID " + id + " no fue encontrado."));

        // Actualizar los campos del usuario existente con los valores del DTO
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setApellido(usuario.getApellido());
        usuarioExistente.setTelefono(usuario.getTelefono());
        usuarioExistente.setEmail(usuario.getEmail());

        return usuarioMapper.toResponseDto(usuarioRepository.save(usuarioExistente));

    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario con ID " + id + " no fue encontrado."));

        usuarioExistente.setEstado(Estado.INACTIVO);
        usuarioRepository.save(usuarioExistente);
    }
}