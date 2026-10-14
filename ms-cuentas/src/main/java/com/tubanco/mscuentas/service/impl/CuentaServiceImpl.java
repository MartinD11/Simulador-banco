package com.tubanco.mscuentas.service.impl;

import com.tubanco.bancocommon.exceptions.RecursoNoEncontradoException;
import com.tubanco.bancocommon.exceptions.TipoCuentaYaExisteException;
import com.tubanco.mscuentas.dto.request.CrearCuentaRequestDto;
import com.tubanco.mscuentas.dto.response.CuentaResponseDto;
import com.tubanco.mscuentas.entity.Cuenta;
import com.tubanco.mscuentas.enums.Estado;
import com.tubanco.mscuentas.mapper.CuentaMapper;
import com.tubanco.mscuentas.repository.CuentaRepository;
import com.tubanco.mscuentas.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
        nuevaCuenta.setEstado(Estado.ACTIVA);
        nuevaCuenta.setCbu(generarCbuFalso());

        //guardo en la base de datos
        Cuenta cuentaGuardada = cuentaRepository.save(nuevaCuenta);

        return cuentaMapper.toDto(cuentaGuardada);

    }

    private String generarCbuFalso() {
        // Un CBU bancario consta de exactamente 22 dígitos
        // Prefijo de 8 dígitos + 14 dígitos aleatorios rellenos con ceros a la izquierda
        long numeroAleatorio = (long) (Math.random() * 100_000_000_000_000L);
        return String.format("00000031%014d", numeroAleatorio); // 8 + 14 = 22 caracteres exactos
    }

    @Override
    public void eliminarCuenta(Long id){
        //pasos:
        //buscar por id si existe la cuenta, si no existe lanzo una excepcion
        //si existe paso ele estado de la cuenta a inactivo
        //guardo la actualizacion en la base de datos
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(()-> new RecursoNoEncontradoException(
                        "No se encontró la cuenta con id: " + id + ". No se puede eliminar una cuenta que no existe."
                ));

        cuenta.setEstado(Estado.CERRADA);

        cuentaRepository.save(cuenta);
    }

    @Override
    public CuentaResponseDto obtenerCuenta(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(()-> new RecursoNoEncontradoException(
                        "No se encontró la cuenta con id: " + id + ". No se puede obtener una cuenta que no existe."
                ));
        return cuentaMapper.toDto(cuenta);
    }

    @Override
    public List<CuentaResponseDto> obtenerCuentasPorUsuario(Long usuarioId) {
        List<CuentaResponseDto> respuesta = cuentaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(cuentaMapper::toDto)
                .toList();

        return respuesta;
    }


}

