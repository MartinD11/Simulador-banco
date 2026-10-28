package com.tubanco.msinversiones.mapper;

import com.tubanco.msinversiones.dto.DolarApiResponseDto;
import com.tubanco.msinversiones.dto.DolarResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DolarMapper {

    DolarResponseDto toResponse(DolarApiResponseDto source);
}
