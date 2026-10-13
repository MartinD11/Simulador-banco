package com.tubanco.mscuentas.service;

import com.tubanco.mscuentas.dto.request.CrearCuentaRequestDto;
import com.tubanco.mscuentas.dto.response.CuentaResponseDto;

public interface CuentaService {
    CuentaResponseDto crearCuenta(CrearCuentaRequestDto requestDto);
}