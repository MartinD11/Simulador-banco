package com.tubanco.msinversiones.mapper;

import com.tubanco.msinversiones.dto.response.DolarApiResponseDto;
import com.tubanco.msinversiones.dto.response.DolarResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DolarMapper {

    DolarResponseDto toResponse(DolarApiResponseDto source);
}
