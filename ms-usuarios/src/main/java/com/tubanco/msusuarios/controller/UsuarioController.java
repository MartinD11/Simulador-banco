package com.tubanco.msusuarios.controller;

import com.tubanco.msusuarios.dto.request.UsuarioRequestDto;
import com.tubanco.msusuarios.dto.response.UsuarioResponseDto;
import com.tubanco.msusuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión y administración de usuarios en el banco")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Operation(summary = "Crear un nuevo usuario", description = "Registra un usuario en el sistema validando que el DNI y el email no estén duplicados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o campos obligatorios faltantes")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> crearUsuario(@RequestBody UsuarioRequestDto usuario) {
        UsuarioResponseDto response = usuarioService.crearUsuario(usuario);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    //preguntar
    @Operation(summary = "Listar todos los usuarios", description = "Retorna una lista con todos los usuarios registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
        List<UsuarioResponseDto> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Buscar usuario por ID", description = "Permite obtener los detalles de un usuario específico buscando por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarUsuarioPorId(@Parameter(description = "ID único del usuario", example = "1")
                                                                     @PathVariable Long id) {
        UsuarioResponseDto response = usuarioService.findById(id);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar usuario por DNI", description = "Permite consultar los datos de un usuario utilizando su número de documento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con ese DNI")
    })
    @GetMapping("/dni/{dni}")
    public ResponseEntity<UsuarioResponseDto> buscarPorDni(
            @Parameter(description = "DNI del usuario (sin puntos)", example = "38123456")
            @PathVariable String dni) {
        UsuarioResponseDto usuario = usuarioService.findByDni(dni);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Actualizar datos de un usuario", description = "Permite modificar datos del perfil (nombre, apellido, teléfono, email) de un usuario existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequestDto usuario) {
        UsuarioResponseDto response = usuarioService.actualizarUsuario(id, usuario);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminación lógica de un usuario", description = "Cambia el estado del usuario a INACTIVO en lugar de borrarlo físicamente de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario dado de baja exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "ID del usuario a dar de baja", example = "1")
            @PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
