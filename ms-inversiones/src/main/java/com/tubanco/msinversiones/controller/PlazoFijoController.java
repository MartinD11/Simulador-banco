package com.tubanco.msinversiones.controller;

import com.tubanco.msinversiones.dto.request.CrearPlazoFijoRequestDto;
import com.tubanco.msinversiones.dto.response.PlazoFijoResponseDto;
import com.tubanco.msinversiones.service.PlazoFijoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inversiones/plazos-fijos")
@RequiredArgsConstructor
public class PlazoFijoController {

    private final PlazoFijoService plazoFijoService;

    @PostMapping
    public ResponseEntity<PlazoFijoResponseDto> crear(
            @Valid @RequestBody CrearPlazoFijoRequestDto request) {

        return ResponseEntity.ok(plazoFijoService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<PlazoFijoResponseDto>> obtenerTodos() {
        return ResponseEntity.ok(plazoFijoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlazoFijoResponseDto> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(plazoFijoService.obtenerPorId(id));
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<PlazoFijoResponseDto>> obtenerPorCuentaId(
            @PathVariable Long cuentaId) {

        return ResponseEntity.ok(
                plazoFijoService.obtenerPorCuentaId(cuentaId)
        );
    }
}