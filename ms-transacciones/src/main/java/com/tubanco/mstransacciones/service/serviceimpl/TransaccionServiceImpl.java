package com.tubanco.mstransacciones.service.serviceimpl;

import com.tubanco.bancocommon.exceptions.CuentaInactivaException;
import com.tubanco.bancocommon.exceptions.RecursoNoEncontradoException;
import com.tubanco.bancocommon.exceptions.SaldoInsuficienteException;
import com.tubanco.mstransacciones.client.CuentaClient;
import com.tubanco.mstransacciones.dto.request.OperacionCuentaRequestDto;
import com.tubanco.mstransacciones.dto.response.CuentaResponseDto;
import com.tubanco.mstransacciones.dto.response.TransaccionResponseDto;
import com.tubanco.mstransacciones.entity.Transaccion;
import com.tubanco.mstransacciones.enums.EstadoCuenta;
import com.tubanco.mstransacciones.enums.EstadoTransaccion;
import com.tubanco.mstransacciones.enums.TipoTransaccion;
import com.tubanco.mstransacciones.mapper.TransaccionMapper;
import com.tubanco.mstransacciones.repository.TransaccionRepository;
import com.tubanco.mstransacciones.service.TransaccionService;
import feign.FeignException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionServiceImpl implements TransaccionService {
    private final TransaccionRepository transaccionRepository;
    private final CuentaClient cuentaClient;
    private final TransaccionMapper transaccionMapper;

    @Override
    @Transactional
    public Long procesarTransaccion(Long cuentaId, BigDecimal monto,Long destinoId) {
        //primera validacion: verificar que el saldo sea un monto lopgico
        if(monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto negativo no puede ser menor que zero");
        }

        CuentaResponseDto cuentaOrigen;
        try {
            cuentaOrigen = cuentaClient.obtenerCuentaPorId(cuentaId);
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("Cuenta origen no encontrada con id: " + cuentaId);
        }

        if(cuentaOrigen.getEstado() != EstadoCuenta.ACTIVA) {
            throw new CuentaInactivaException("La cuenta origen no se encuentra activa");
        }

        CuentaResponseDto cuentaDestino;
        try {
            cuentaDestino = cuentaClient.obtenerCuentaPorId(destinoId);
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("Cuenta origen no encontrada con id: " + cuentaId);
        }
        if(cuentaDestino.getEstado() != EstadoCuenta.ACTIVA) {
            throw new CuentaInactivaException("La cuenta origen no se encuentra activa");
        }

        if(cuentaOrigen.getSaldo().compareTo(monto) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta origen");
        }


        Transaccion transaccion = Transaccion.builder()
                .cuentaOrigenId(cuentaId)
                .cbuOrigen(cuentaOrigen.getCbu())
                .titularOrigen(cuentaOrigen.getTitular())
                .cuentaDestinoId(destinoId)
                .cbuDestino(cuentaDestino.getCbu())
                .titularDestino(cuentaDestino.getTitular())
                .monto(monto)
                .tipo(TipoTransaccion.TRANSFERENCIA)
                .estado(EstadoTransaccion.PENDIENTE)
                .fechaTransaccion(LocalDateTime.now())
                .build();

        transaccionRepository.save(transaccion);

        cuentaClient.actualizarSaldo(cuentaId, monto.negate());
        cuentaClient.actualizarSaldo(destinoId, monto);

        transaccion.setEstado(EstadoTransaccion.COMPLETADA);
        Transaccion guardada = transaccionRepository.save(transaccion);

        return guardada.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public TransaccionResponseDto obtenerPorId(Long id) {
        Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(()-> new RecursoNoEncontradoException("Transaccion no encontrada con id: " + id));

        return transaccionMapper.toDto(transaccion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransaccionResponseDto> obtenerHistorialPorCuenta(Long cuentaId) {
        List<TransaccionResponseDto> transacciones = transaccionRepository.findByCuentaOrigenIdOrCuentaDestinoIdOrderByFechaTransaccionDesc(cuentaId, cuentaId)
                .stream()
                .map(transaccionMapper::toDto)
                .toList();

        return transacciones;
    }

    @Override
    @Transactional
    public TransaccionResponseDto realizarDeposito(OperacionCuentaRequestDto dto) {
        CuentaResponseDto cuenta = obtenerCuentaYValidarActiva(dto.getCuentaId());

        // 1. Impactar el saldo positivo en ms-cuentas
        cuentaClient.actualizarSaldo(cuenta.getId(), dto.getMonto());

        // 2. Registrar comprobante de depósito
        Transaccion transaccion = Transaccion.builder()
                .cuentaDestinoId(cuenta.getId())
                .cbuDestino(cuenta.getCbu())
                .titularDestino(cuenta.getTitular())
                .monto(dto.getMonto())
                .tipo(TipoTransaccion.DEPOSITO)
                .estado(EstadoTransaccion.COMPLETADA)
                .fechaTransaccion(LocalDateTime.now())
                .build();

        Transaccion guardada = transaccionRepository.save(transaccion);
        return transaccionMapper.toDto(guardada);
    }

    @Override
    @Transactional
    public TransaccionResponseDto realizarExtraccion(OperacionCuentaRequestDto dto) {
        CuentaResponseDto cuenta = obtenerCuentaYValidarActiva(dto.getCuentaId());

        if (cuenta.getSaldo().compareTo(dto.getMonto()) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar la extracción");
        }

        cuentaClient.actualizarSaldo(cuenta.getId(), dto.getMonto().negate());

        Transaccion transaccion = Transaccion.builder()
                .cuentaOrigenId(cuenta.getId())
                .cbuOrigen(cuenta.getCbu())
                .titularOrigen(cuenta.getTitular())
                .monto(dto.getMonto())
                .tipo(TipoTransaccion.EXTRACCION)
                .estado(EstadoTransaccion.COMPLETADA)
                .fechaTransaccion(LocalDateTime.now())
                .build();

        Transaccion guardada = transaccionRepository.save(transaccion);
        return transaccionMapper.toDto(guardada);
    }

    private CuentaResponseDto obtenerCuentaYValidarActiva(Long cuentaId) {
        CuentaResponseDto cuenta;
        try {
            cuenta = cuentaClient.obtenerCuentaPorId(cuentaId);
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("Cuenta no encontrada con id: " + cuentaId);
        }

        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            throw new CuentaInactivaException("La cuenta no se encuentra activa");
        }

        return cuenta;
    }
}
