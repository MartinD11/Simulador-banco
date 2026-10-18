package com.tubanco.msusuarios.dto.response;

import com.tubanco.msusuarios.enums.Estado;
import com.tubanco.msusuarios.enums.Rol;
import lombok.Data;

@Data
public class UsuarioResponseDto {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private Estado estado;
    private Rol rol;

}