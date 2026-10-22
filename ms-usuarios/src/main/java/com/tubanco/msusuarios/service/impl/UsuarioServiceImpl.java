package com.tubanco.msusuarios.service.impl;

import com.tubanco.bancocommon.exceptions.RecursoNoEncontradoException;
import com.tubanco.msusuarios.context.ContextoUsuario;
import com.tubanco.msusuarios.dto.request.UsuarioRequestDto;
import com.tubanco.msusuarios.dto.response.UsuarioResponseDto;
import com.tubanco.msusuarios.entity.Usuario;
import com.tubanco.msusuarios.enums.Estado;
import com.tubanco.msusuarios.mapper.UsuarioMapper;
import com.tubanco.msusuarios.repository.UsuarioRepository;
import com.tubanco.msusuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final ContextoUsuario contextoUsuario;

    @Override
    @Transactional
    @CacheEvict(value = "usuarios", allEntries = true) // Purga la caché completa para evitar datos fantasmas
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
    @Cacheable(value = "usuarios", key = "'todos'")
    public List<UsuarioResponseDto> findAll() {
        validarSoloAdmin();

        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "usuarios", key = "#id")
    public UsuarioResponseDto findById(Long id) {
        validarPropiedadOAdmin(id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(()-> new RecursoNoEncontradoException("El usuario con ID " + id + " no fue encontrado."));

        return usuarioMapper.toResponseDto(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDto findByDni(String dni) {
        Usuario usuario = usuarioRepository.findByDni(dni)
                .orElseThrow(()-> new RecursoNoEncontradoException("El usuario con DNI " + dni + " no fue encontrado."));

        validarPropiedadOAdmin(usuario.getId());

        return usuarioMapper.toResponseDto(usuario);
    }

    @Override
    @Transactional
    @CacheEvict(value = "usuarios", key = "#id") // Borra únicamente el registro modificado
    public UsuarioResponseDto actualizarUsuario(Long id, UsuarioRequestDto usuario) {
        validarPropiedadOAdmin(id);

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario con ID " + id + " no fue encontrado."));

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setApellido(usuario.getApellido());
        usuarioExistente.setTelefono(usuario.getTelefono());
        usuarioExistente.setEmail(usuario.getEmail());

        return usuarioMapper.toResponseDto(usuarioRepository.save(usuarioExistente));
    }

    @Override
    @Transactional
    @CacheEvict(value = "usuarios", allEntries = true) // Borra todo para mantener la lista global sincronizada
    public void eliminarUsuario(Long id) {
        validarPropiedadOAdmin(id);

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario con ID " + id + " no fue encontrado."));

        usuarioExistente.setEstado(Estado.INACTIVO);
        usuarioRepository.save(usuarioExistente);
    }

    private void validarPropiedadOAdmin(Long idPerfil) {
        if (contextoUsuario.isLlamadaInterna() || contextoUsuario.isAdmin()) {
            return;
        }
        Long usuarioActualId = contextoUsuario.getUsuarioId();
        if (usuarioActualId == null || !idPerfil.equals(usuarioActualId)) {
            throw new RuntimeException("Acceso Denegado: No tenés permisos sobre este perfil.");
        }
    }

    private void validarSoloAdmin() {
        if (!contextoUsuario.isAdmin()) {
            throw new RuntimeException("Acceso Denegado: Operación exclusiva para administradores.");
        }
    }
}