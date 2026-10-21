package com.tubanco.msusuarios.service.impl;

import com.tubanco.bancocommon.exceptions.RecursoNoEncontradoException;
import com.tubanco.msusuarios.dto.request.AuthRequestDto;
import com.tubanco.msusuarios.dto.request.UsuarioRequestDto;
import com.tubanco.msusuarios.dto.response.UsuarioResponseDto;
import com.tubanco.msusuarios.entity.Usuario;
import com.tubanco.msusuarios.repository.UsuarioRepository;
import com.tubanco.msusuarios.security.JwtProvider;
import com.tubanco.msusuarios.service.AuthService;
import com.tubanco.msusuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioService usuarioService; // INYECTAMOS TU SERVICIO
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public UsuarioResponseDto registrar(UsuarioRequestDto dto) {
        // 1. Interceptamos la contraseña plana y la encriptamos con BCrypt
        String passwordHasheada = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(passwordHasheada);

        // 2. Delegamos la creación a tu lógica de negocio (que ya valida DNI y usa MapStruct)
        return usuarioService.crearUsuario(dto);
    }

    @Override
    public String login(AuthRequestDto dto) {
        // 1. Buscar usuario por email (requiere el findByEmail en el repo)
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RecursoNoEncontradoException("Credenciales inválidas"));

        // 2. Comparar la contraseña ingresada (plana) con la guardada (hasheada)
        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // 3. Si es correcto, fabricamos y devolvemos el token
        return jwtProvider.generarToken(usuario.getEmail(), usuario.getId(),usuario.getRol().name());
    }
}