package com.tubanco.mstransacciones.service;

import com.tubanco.mstransacciones.dto.response.TransaccionResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface TransaccionService {
    Long procesarTransaccion(Long cuentaId, BigDecimal monto,Long destinoId);
    TransaccionResponseDto obtenerPorId(Long id);
    List<TransaccionResponseDto> obtenerHistorialPorCuenta(Long cuentaId);

}
