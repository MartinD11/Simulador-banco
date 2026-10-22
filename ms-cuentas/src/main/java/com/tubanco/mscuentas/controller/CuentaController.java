package com.tubanco.mscuentas.controller;

import com.tubanco.mscuentas.dto.request.CrearCuentaRequestDto;
import com.tubanco.mscuentas.dto.response.CuentaResponseDto;
import com.tubanco.mscuentas.service.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
@Tag(name = "Cuentas", description = "Operaciones relacionadas con la gestión de cuentas bancarias")
public class CuentaController {
    private final CuentaService cuentaService;


    @Operation(summary = "Crear una nueva cuenta", description = "Genera una nueva cuenta bancaria para un usuario existente validando que no posea duplicados del mismo tipo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o faltantes", content = @Content),
            @ApiResponse(responseCode = "409", description = "El usuario ya posee una cuenta de este tipo", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CuentaResponseDto> crearCuenta( @Valid @RequestBody CrearCuentaRequestDto requestDto){
        CuentaResponseDto response =  cuentaService.crearCuenta(requestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Eliminar una cuenta", description = "Da de baja una cuenta existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCuenta(@PathVariable Long id){
        cuentaService.eliminarCuenta(id);
        return ResponseEntity.ok("La cuenta ha sido dada de baja correctamente.");
    }

    @Operation(summary = "Obtener una cuenta por ID", description = "Recupera los detalles de una cuenta específica utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada y retornada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDto> obtenerCuenta(@PathVariable Long id){
        CuentaResponseDto response = cuentaService.obtenerCuenta(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener cuentas por usuario", description = "Recupera todas las cuentas asociadas a un usuario específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuentas encontradas y retornadas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<CuentaResponseDto>> obtenerCuentasPorUsuario(@PathVariable Long id){
        List<CuentaResponseDto> response = cuentaService.obtenerCuentasPorUsuario(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/actualizar-saldo")
    public ResponseEntity<Void> actualizarSaldo(@PathVariable Long id, @RequestParam BigDecimal monto,
                                                @RequestHeader(value = "X-Llamada-Interna", required = false) String llamadaInterna) {

        if (!"true".equals(llamadaInterna)) {
            // Si no tiene el sello (porque vino de afuera o de un lugar no autorizado), lo rebotamos
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        cuentaService.actualizarSaldo(id, monto);
        return ResponseEntity.ok().build();
    }

}