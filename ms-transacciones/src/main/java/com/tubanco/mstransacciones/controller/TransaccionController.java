package com.tubanco.mstransacciones.controller;

import com.tubanco.mstransacciones.dto.request.OperacionCuentaRequestDto;
import com.tubanco.mstransacciones.dto.request.TransferenciaRequestDto;
import com.tubanco.mstransacciones.dto.response.TransaccionResponseDto;
import com.tubanco.mstransacciones.entity.Transaccion;
import com.tubanco.mstransacciones.service.TransaccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
public class TransaccionController {
    private final TransaccionService transaccionService;

    @PostMapping("/transferencias")
    public ResponseEntity<TransferenciaRequestDto> transferencias(@RequestBody @Valid TransferenciaRequestDto transaccion){
        Long nuevaTransaccionId = transaccionService.procesarTransaccion(
                transaccion.getCuentaOrigenId(),
                transaccion.getMonto(),
                transaccion.getCuentaDestinoId()
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaTransaccionId)
                .toUri();

        return  ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDto> transacciones(@PathVariable Long id){
        TransaccionResponseDto response =  transaccionService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<TransaccionResponseDto>> obtenerHistorialPorCuenta(@PathVariable Long cuentaId) {
        List<TransaccionResponseDto> historial = transaccionService.obtenerHistorialPorCuenta(cuentaId);
        return ResponseEntity.ok(historial);
    }

    @PostMapping("/depositos")
    public ResponseEntity<TransaccionResponseDto> realizarDeposito(@Valid @RequestBody OperacionCuentaRequestDto dto) {
        TransaccionResponseDto respuesta = transaccionService.realizarDeposito(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/transacciones/{id}")
                .buildAndExpand(respuesta.getId())
                .toUri();
        return ResponseEntity.created(location).body(respuesta);
    }

    @PostMapping("/extracciones")
    public ResponseEntity<TransaccionResponseDto> realizarExtraccion(@Valid @RequestBody OperacionCuentaRequestDto dto) {
        TransaccionResponseDto respuesta = transaccionService.realizarExtraccion(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/transacciones/{id}")
                .buildAndExpand(respuesta.getId())
                .toUri();
        return ResponseEntity.created(location).body(respuesta);
    }
}
