package com.tubanco.mscuentas.dto.response;

import com.tubanco.mscuentas.enums.EstadoUsuario;
import com.tubanco.mscuentas.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private EstadoUsuario estado;
    private RolUsuario rol;
}