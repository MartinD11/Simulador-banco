package com.tubanco.msinversiones.controller;

import com.tubanco.msinversiones.dto.DolarResponseDto;
import com.tubanco.msinversiones.enums.DolarTipo;
import com.tubanco.msinversiones.service.DolarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/inversiones/dolar")
@RequiredArgsConstructor
public class DolarController {

    private final DolarService dolarService;

    @GetMapping("/{tipo}")
    public ResponseEntity<DolarResponseDto> obtenerDolar(
            @PathVariable String tipo) {

        DolarTipo dolarTipo = DolarTipo.valueOf(
                tipo.toUpperCase(Locale.ROOT)
        );

        return ResponseEntity.ok(dolarService.obtenerDolar(dolarTipo));
    }
}