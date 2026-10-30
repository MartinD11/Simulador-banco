package com.tubanco.msinversiones.service;

import com.tubanco.msinversiones.dto.response.DolarResponseDto;
import com.tubanco.msinversiones.enums.DolarTipo;

public interface DolarService {

    DolarResponseDto obtenerDolar(DolarTipo tipo);
}