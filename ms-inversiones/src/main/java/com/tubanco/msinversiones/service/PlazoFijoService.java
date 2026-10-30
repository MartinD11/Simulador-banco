package com.tubanco.msinversiones.service;

import com.tubanco.msinversiones.dto.request.CrearPlazoFijoRequestDto;
import com.tubanco.msinversiones.dto.response.PlazoFijoResponseDto;

import java.util.List;

public interface PlazoFijoService {

    PlazoFijoResponseDto crear(CrearPlazoFijoRequestDto request);

    List<PlazoFijoResponseDto> obtenerTodos();

    PlazoFijoResponseDto obtenerPorId(Long id);

    void procesarVencimientos();

    List<PlazoFijoResponseDto> obtenerPorCuentaId(Long cuentaId);
}
