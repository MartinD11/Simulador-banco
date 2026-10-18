package com.tubanco.msusuarios.mapper;

import com.tubanco.msusuarios.dto.request.UsuarioRequestDto;
import com.tubanco.msusuarios.dto.response.UsuarioResponseDto;
import com.tubanco.msusuarios.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    // Convierte de Request DTO a Entidad
    Usuario toEntity(UsuarioRequestDto dto);

    // Convierte de Entidad a Response DTO
    UsuarioResponseDto toResponseDto(Usuario usuario);

    // Actualiza una entidad existente a partir del DTO
    void updateEntityFromDto(UsuarioRequestDto dto, @MappingTarget Usuario usuario);
}