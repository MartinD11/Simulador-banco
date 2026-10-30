package com.tubanco.msinversiones.service.impl;

import com.tubanco.msinversiones.dto.response.DolarApiResponseDto;
import com.tubanco.msinversiones.dto.response.DolarResponseDto;
import com.tubanco.msinversiones.enums.DolarTipo;
import com.tubanco.msinversiones.mapper.DolarMapper;
import com.tubanco.msinversiones.service.DolarService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class DolarServiceImpl implements DolarService {

    private final RestClient dolarRestClient;
    private final DolarMapper dolarMapper;

    @Override
    @Cacheable(value = "dolar", key = "#tipo.name()")
    public DolarResponseDto obtenerDolar(DolarTipo tipo) {
        String endpoint = switch (tipo) {
            case OFICIAL -> "oficial";
            case BLUE -> "blue";
            case MEP -> "bolsa";
        };

        DolarApiResponseDto respuestaExterna = dolarRestClient
                .get()
                .uri("/dolares/" + endpoint)
                .retrieve()
                .body(DolarApiResponseDto.class);

        return dolarMapper.toResponse(respuestaExterna);
    }
}
