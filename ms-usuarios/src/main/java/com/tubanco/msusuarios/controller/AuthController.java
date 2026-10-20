package com.tubanco.msusuarios.controller;

import com.tubanco.msusuarios.dto.request.AuthRequestDto;
import com.tubanco.msusuarios.dto.request.UsuarioRequestDto;
import com.tubanco.msusuarios.dto.response.AuthResponseDto;
import com.tubanco.msusuarios.dto.response.UsuarioResponseDto;
import com.tubanco.msusuarios.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDto> registrar(@Valid @RequestBody UsuarioRequestDto dto) {
        UsuarioResponseDto respuesta = authService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto dto) {
        String token = authService.login(dto);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}