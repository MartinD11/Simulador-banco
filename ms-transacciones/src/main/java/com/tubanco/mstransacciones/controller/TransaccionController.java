package com.tubanco.mstransacciones.controller;

import com.tubanco.mstransacciones.dto.request.TransferenciaRequestDto;
import com.tubanco.mstransacciones.entity.Transaccion;
import com.tubanco.mstransacciones.service.TransaccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
}
