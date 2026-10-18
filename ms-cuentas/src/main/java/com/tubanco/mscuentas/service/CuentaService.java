package com.tubanco.mscuentas.service;

import com.tubanco.mscuentas.dto.request.CrearCuentaRequestDto;
import com.tubanco.mscuentas.dto.response.CuentaResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    CuentaResponseDto crearCuenta(CrearCuentaRequestDto requestDto);
    void eliminarCuenta(Long id);
    CuentaResponseDto obtenerCuenta(Long id);
    List<CuentaResponseDto> obtenerCuentasPorUsuario(Long id);
    void actualizarSaldo(Long id, BigDecimal monto);
}