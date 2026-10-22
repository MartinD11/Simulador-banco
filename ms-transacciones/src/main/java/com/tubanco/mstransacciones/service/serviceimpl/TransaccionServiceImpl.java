package com.tubanco.mstransacciones.service.serviceimpl;

import com.tubanco.bancocommon.exceptions.CuentaInactivaException;
import com.tubanco.bancocommon.exceptions.RecursoNoEncontradoException;
import com.tubanco.bancocommon.exceptions.SaldoInsuficienteException;
import com.tubanco.mstransacciones.client.CuentaClient;
import com.tubanco.mstransacciones.context.ContextoUsuario;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionServiceImpl implements TransaccionService {
    private final TransaccionRepository transaccionRepository;
    private final CuentaClient cuentaClient;
    private final TransaccionMapper transaccionMapper;
    private final ContextoUsuario contextUsuario;

    @Override
    @Transactional
    public Long procesarTransaccion(Long cuentaId, BigDecimal monto, Long destinoId) {
        if(monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto no puede ser menor o igual a cero");
        }

        // validamos la seguridad de la cuenta de origen
        CuentaResponseDto cuentaOrigen = obtenerCuentaYValidarActiva(cuentaId);
        validarPropiedadOAdmin(cuentaOrigen);

        //  Obtenemos cuenta destino (no validamos propiedad porque cualquiera puede recibir plata)
        CuentaResponseDto cuentaDestino = obtenerCuentaYValidarActiva(destinoId);

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

        // Para ver el comprobante, validamos que el usuario sea el dueño de la cuenta origen o destino (o ADMIN)
        if (!contextUsuario.isAdmin()) {
            Long usuarioActualId = contextUsuario.getUsuarioId();
            CuentaResponseDto cuentaOrigen = cuentaClient.obtenerCuentaPorId(transaccion.getCuentaOrigenId());
            boolean esOrigen = usuarioActualId != null && usuarioActualId.equals(cuentaOrigen.getUsuarioId());

            boolean esDestino = false;
            if (transaccion.getCuentaDestinoId() != null) {
                CuentaResponseDto cuentaDestino = cuentaClient.obtenerCuentaPorId(transaccion.getCuentaDestinoId());
                esDestino = usuarioActualId != null && usuarioActualId.equals(cuentaDestino.getUsuarioId());
            }

            if (!esOrigen && !esDestino) {
                throw new RuntimeException("Acceso Denegado: No participaste de esta transacción.");
            }
        }

        return transaccionMapper.toDto(transaccion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransaccionResponseDto> obtenerHistorialPorCuenta(Long cuentaId) {
        // Validamos que la persona que pide el historial sea el dueño de la cuenta
        CuentaResponseDto cuenta = obtenerCuentaYValidarActiva(cuentaId);
        validarPropiedadOAdmin(cuenta);

        return transaccionRepository.findByCuentaOrigenIdOrCuentaDestinoIdOrderByFechaTransaccionDesc(cuentaId, cuentaId)
                .stream()
                .map(transaccionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public TransaccionResponseDto realizarDeposito(OperacionCuentaRequestDto dto) {
        CuentaResponseDto cuenta = obtenerCuentaYValidarActiva(dto.getCuentaId());

        // Dependiendo del negocio, quizás un tercero puede depositarte,
        // pero por seguridad bloqueamos para que solo el dueño o un cajero (admin) deposite.
        validarPropiedadOAdmin(cuenta);

        cuentaClient.actualizarSaldo(cuenta.getId(), dto.getMonto());

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
        validarPropiedadOAdmin(cuenta);

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

    private void validarPropiedadOAdmin(CuentaResponseDto cuenta) {
        if (contextUsuario.isAdmin()) {
            return;
        }

        Long usuarioActualId = contextUsuario.getUsuarioId();
        if (usuarioActualId == null || !usuarioActualId.equals(cuenta.getUsuarioId())) {
            throw new RuntimeException("Acceso Denegado: No tenés permisos sobre esta cuenta.");
        }
    }
}
