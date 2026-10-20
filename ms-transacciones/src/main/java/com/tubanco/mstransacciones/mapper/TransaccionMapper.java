package com.tubanco.mstransacciones.mapper;

import com.tubanco.mstransacciones.dto.response.TransaccionResponseDto;
import com.tubanco.mstransacciones.entity.Transaccion;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransaccionMapper {
    TransaccionResponseDto toDto(Transaccion transaccion);

    List<TransaccionResponseDto> toDtoList(List<Transaccion> transacciones);
}