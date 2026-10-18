package com.tubanco.mscuentas.service.impl;

import com.tubanco.bancocommon.exceptions.TipoCuentaYaExisteException;
import com.tubanco.mscuentas.dto.request.CrearCuentaRequestDto;
import com.tubanco.mscuentas.dto.response.CuentaResponseDto;
import com.tubanco.mscuentas.entity.Cuenta;
import com.tubanco.mscuentas.mapper.CuentaMapper;
import com.tubanco.mscuentas.repository.CuentaRepository;
import com.tubanco.mscuentas.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    @Override
    public CuentaResponseDto crearCuenta(CrearCuentaRequestDto requestDto) {
        //primero valido si existe la cuenta para el usuario y el tipo de cuenta
        //si existe lanzo una excepcion
        //si no existe creo la cuenta y la guardo en la base de datos
        boolean existeCuenta = cuentaRepository.existsByUsuarioIdAndTipoCuenta(requestDto.getUsuarioId(), requestDto.getTipoCuenta());

        if(existeCuenta){
            throw new TipoCuentaYaExisteException(
                    "El usuario ya posee una cuenta del tipo " + requestDto.getTipoCuenta() + ". No se puede crear otra cuenta del mismo tipo para el mismo usuario."
            );
        }

        //mapeo el dto a entidad para guardarlo en la bases de datos
        Cuenta nuevaCuenta = cuentaMapper.toEntity(requestDto);

        nuevaCuenta.setSaldo(BigDecimal.ZERO);
        nuevaCuenta.setCbu(generarCbuFalso());

        //guardo en la base de datos
        Cuenta cuentaGuardada = cuentaRepository.save(nuevaCuenta);

        return cuentaMapper.toDto(cuentaGuardada);

    }

    private String generarCbuFalso() {
        return "00000031" + (long) (Math.random() * 100000000000000L);
    }
}
