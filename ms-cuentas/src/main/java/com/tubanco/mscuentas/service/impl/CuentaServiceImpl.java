package com.tubanco.mscuentas.service.impl;

import com.tubanco.bancocommon.exceptions.RecursoNoEncontradoException;
import com.tubanco.bancocommon.exceptions.TipoCuentaYaExisteException;
import com.tubanco.mscuentas.client.UsuarioClient;
import com.tubanco.mscuentas.context.ContextoUsuario;
import com.tubanco.mscuentas.dto.request.CrearCuentaRequestDto;
import com.tubanco.mscuentas.dto.response.CuentaResponseDto;
import com.tubanco.mscuentas.dto.response.UsuarioResponseDto;
import com.tubanco.mscuentas.entity.Cuenta;
import com.tubanco.mscuentas.entity.Tarjeta;
import com.tubanco.mscuentas.enums.Estado;
import com.tubanco.mscuentas.enums.EstadoUsuario;
import com.tubanco.mscuentas.mapper.CuentaMapper;
import com.tubanco.mscuentas.repository.CuentaRepository;
import com.tubanco.mscuentas.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;
    private final UsuarioClient usuarioClient;
    private final ContextoUsuario contextoUsuario;

    @Override
    public CuentaResponseDto crearCuenta(CrearCuentaRequestDto requestDto) {
        validarPropiedadOAdmin(requestDto.getUsuarioId());

        UsuarioResponseDto usuario;
        try {
            usuario = usuarioClient.buscarUsuarioPorId(requestDto.getUsuarioId());
        } catch (Exception e) {
            System.err.println("================ ERROR REAL DE FEIGN ================");
            e.printStackTrace();
            throw new RuntimeException("Error real: " + e.getMessage(), e);
        }

        if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
            throw new IllegalStateException("El usuario con ID " + requestDto.getUsuarioId() + " se encuentra inactivo. No se puede crear una cuenta.");
        }

        boolean existeCuenta = cuentaRepository.existsByUsuarioIdAndTipoCuenta(requestDto.getUsuarioId(), requestDto.getTipoCuenta());

        if (existeCuenta) {
            throw new TipoCuentaYaExisteException(
                    "El usuario ya posee una cuenta del tipo " + requestDto.getTipoCuenta() + ". No se puede crear otra cuenta del mismo tipo."
            );
        }

        Cuenta nuevaCuenta = cuentaMapper.toEntity(requestDto);
        nuevaCuenta.setTitular(usuario.getNombre() + " " + usuario.getApellido());
        nuevaCuenta.setSaldo(BigDecimal.ZERO);
        nuevaCuenta.setEstado(Estado.ACTIVA);
        nuevaCuenta.setCbu(generarCbuFalso());

        Tarjeta nuevaTarjeta = Tarjeta.builder()
                .numeroTarjeta(generarNumeroTarjeta())
                .cvv(generarCvv())
                .fechaVencimiento(LocalDate.now().plusYears(5))
                .cuenta(nuevaCuenta)
                .build();

        nuevaCuenta.setTarjeta(nuevaTarjeta);

        Cuenta cuentaGuardada = cuentaRepository.save(nuevaCuenta);

        return cuentaMapper.toDto(cuentaGuardada);
    }

    private String generarCbuFalso() {
        long numeroAleatorio = (long) (Math.random() * 100_000_000_000_000L);
        return String.format("00000031%014d", numeroAleatorio);
    }

    @Override
    public void eliminarCuenta(Long id){
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(()-> new RecursoNoEncontradoException(
                        "No se encontró la cuenta con id: " + id + ". No se puede eliminar una cuenta que no existe."
                ));

        validarPropiedadOAdmin(cuenta.getUsuarioId());

        cuenta.setEstado(Estado.CERRADA);

        cuentaRepository.save(cuenta);
    }

    @Override
    public CuentaResponseDto obtenerCuenta(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(()-> new RecursoNoEncontradoException(
                        "No se encontró la cuenta con id: " + id + ". No se puede obtener una cuenta que no existe."
                ));

        validarPropiedadOAdmin(cuenta.getUsuarioId());

        return cuentaMapper.toDto(cuenta);
    }

    @Override
    public List<CuentaResponseDto> obtenerCuentasPorUsuario(Long usuarioId) {
        validarPropiedadOAdmin(usuarioId);

        List<CuentaResponseDto> respuesta = cuentaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(cuentaMapper::toDto)
                .toList();

        return respuesta;
    }

    private String generarNumeroTarjeta() {
        long numeroAleatorio = (long) (Math.random() * 1_000_000_000_000_000L);
        return String.format("4%015d", numeroAleatorio);
    }

    private String generarCvv() {
        int cvvAleatorio = (int) (Math.random() * 1000);
        return String.format("%03d", cvvAleatorio);
    }

    @Override
    public void actualizarSaldo(Long id, BigDecimal monto) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la cuenta con id: " + id + ". No se puede actualizar el saldo de una cuenta que no existe."
                ));

        if(cuenta.getEstado() != Estado.ACTIVA) {
            throw new IllegalStateException("No se puede actualizar el saldo de una cuenta inactiva.");
        }

        cuenta.setSaldo(cuenta.getSaldo().add(monto));

        cuentaRepository.save(cuenta);
    }

    private void validarPropiedadOAdmin(Long usuarioIdPropietario) {
        if (contextoUsuario.isLlamadaInterna() || contextoUsuario.isAdmin()) {
            return;
        }

        Long usuarioActualId = contextoUsuario.getUsuarioId();
        if (usuarioActualId == null || !usuarioIdPropietario.equals(usuarioActualId)) {
            throw new RuntimeException("Acceso Denegado: No tenés permisos sobre esta cuenta.");
        }
    }
}
