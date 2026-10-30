package com.tubanco.msinversiones.mapper;

import com.tubanco.msinversiones.dto.request.CrearPlazoFijoRequestDto;
import com.tubanco.msinversiones.dto.response.PlazoFijoResponseDto;
import com.tubanco.msinversiones.entity.PlazoFijo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlazoFijoMapper {

    PlazoFijo toEntity(CrearPlazoFijoRequestDto request);

    PlazoFijoResponseDto toResponse(PlazoFijo plazoFijo);
}