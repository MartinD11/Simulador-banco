package com.tubanco.mscuentas.mapper;

import com.tubanco.mscuentas.dto.request.CrearCuentaRequestDto;
import com.tubanco.mscuentas.dto.response.CuentaResponseDto;
import com.tubanco.mscuentas.dto.response.TarjetaResponseDto;
import com.tubanco.mscuentas.entity.Cuenta;
import com.tubanco.mscuentas.entity.Tarjeta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// componentModel = "spring" le dice a MapStruct que convierta esta interfaz en un @Component
// para que podamos inyectarla fácilmente en el Service.
@Mapper(componentModel = "spring")
public interface CuentaMapper {

    // Ignoramos id, cbu y saldo porque esos campos no vienen del cliente,
    // los vamos a autogenerar nosotros en la capa de Service.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cbu", ignore = true)
    @Mapping(target = "saldo", ignore = true)
    @Mapping(target = "tarjeta", ignore = true)
    Cuenta toEntity(CrearCuentaRequestDto requestDto);

    CuentaResponseDto toDto(Cuenta cuenta);

    TarjetaResponseDto tarjetaToTarjetaDto(Tarjeta tarjeta);
}