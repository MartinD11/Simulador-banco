package com.tubanco.msinversiones.controller;

import com.tubanco.msinversiones.dto.request.CrearPlazoFijoRequestDto;
import com.tubanco.msinversiones.dto.response.PlazoFijoResponseDto;
import com.tubanco.msinversiones.service.PlazoFijoService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inversiones/plazos-fijos")
@RequiredArgsConstructor
@Tag(
        name = "Plazos fijos",
        description = "Operaciones para crear y consultar inversiones a plazo fijo"
)
public class PlazoFijoController {

    private final PlazoFijoService plazoFijoService;

    @PostMapping
    @Operation(
            summary = "Crear un plazo fijo",
            description = "Debita el monto de la cuenta indicada, calcula los intereses y crea un plazo fijo en estado ACTIVO."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Plazo fijo creado correctamente",
                    content = @Content(schema = @Schema(implementation = PlazoFijoResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content)
    })
    public ResponseEntity<PlazoFijoResponseDto> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para constituir el plazo fijo",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CrearPlazoFijoRequestDto.class)
                    )
            )
            @Valid @RequestBody CrearPlazoFijoRequestDto request) {

        return ResponseEntity.ok(plazoFijoService.crear(request));
    }

    @GetMapping
    @Operation(
            summary = "Obtener todos los plazos fijos",
            description = "Devuelve todos los plazos fijos registrados en el microservicio de inversiones."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Listado obtenido correctamente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            type = "array",
                            implementation = PlazoFijoResponseDto.class
                    )
            )
    )
    public ResponseEntity<List<PlazoFijoResponseDto>> obtenerTodos() {
        return ResponseEntity.ok(plazoFijoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener un plazo fijo por ID",
            description = "Devuelve el detalle de un plazo fijo específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Plazo fijo encontrado",
                    content = @Content(schema = @Schema(implementation = PlazoFijoResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Plazo fijo no encontrado", content = @Content)
    })
    public ResponseEntity<PlazoFijoResponseDto> obtenerPorId(
            @Parameter(description = "Identificador del plazo fijo", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(plazoFijoService.obtenerPorId(id));
    }

    @GetMapping("/cuenta/{cuentaId}")
    @Operation(
            summary = "Obtener plazos fijos por cuenta",
            description = "Devuelve todas las inversiones asociadas a una cuenta bancaria."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "array",
                                    implementation = PlazoFijoResponseDto.class
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content)
    })
    public ResponseEntity<List<PlazoFijoResponseDto>> obtenerPorCuentaId(
            @Parameter(description = "Identificador de la cuenta", example = "1")
            @PathVariable Long cuentaId) {

        return ResponseEntity.ok(
                plazoFijoService.obtenerPorCuentaId(cuentaId)
        );
    }
}