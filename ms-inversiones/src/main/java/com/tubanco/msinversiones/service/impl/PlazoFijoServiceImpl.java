package com.tubanco.msinversiones.service.impl;

import com.tubanco.msinversiones.client.CuentaClient;
import com.tubanco.msinversiones.dto.request.AcreditarFondosRequestDto;
import com.tubanco.msinversiones.dto.request.CrearPlazoFijoRequestDto;
import com.tubanco.msinversiones.dto.request.DebitarFondosRequestDto;
import com.tubanco.msinversiones.dto.response.PlazoFijoResponseDto;
import com.tubanco.msinversiones.entity.PlazoFijo;
import com.tubanco.msinversiones.enums.EstadoPlazoFijo;
import com.tubanco.msinversiones.mapper.PlazoFijoMapper;
import com.tubanco.msinversiones.repository.PlazoFijoRepository;
import com.tubanco.msinversiones.service.PlazoFijoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlazoFijoServiceImpl implements PlazoFijoService {

    private static final BigDecimal DIAS_DEL_ANIO = BigDecimal.valueOf(365);
    private static final int ESCALA_MONETARIA = 2;

    private final PlazoFijoRepository plazoFijoRepository;
    private final PlazoFijoMapper plazoFijoMapper;
    private final CuentaClient cuentaClient;

    @Value("${plazo-fijo.tna}")
    private BigDecimal tna;

    @Override
    @Transactional
    public PlazoFijoResponseDto crear(CrearPlazoFijoRequestDto request) {
        BigDecimal interes = calcularInteres(
                request.getMonto(),
                request.getPlazoDias()
        );

        BigDecimal montoAlVencimiento = request.getMonto()
                .add(interes)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        DebitarFondosRequestDto debito = new DebitarFondosRequestDto();
        debito.setMonto(request.getMonto());

        cuentaClient.debitarFondos(request.getCuentaId(), debito);

        LocalDate fechaConstitucion = LocalDate.now();
        LocalDate fechaVencimiento = fechaConstitucion.plusDays(
                request.getPlazoDias()
        );

        PlazoFijo plazoFijo = plazoFijoMapper.toEntity(request);
        plazoFijo.setTna(tna);
        plazoFijo.setInteresGenerado(interes);
        plazoFijo.setMontoAlVencimiento(montoAlVencimiento);
        plazoFijo.setFechaConstitucion(fechaConstitucion);
        plazoFijo.setFechaVencimiento(fechaVencimiento);
        plazoFijo.setEstado(EstadoPlazoFijo.ACTIVO);

        return plazoFijoMapper.toResponse(
                plazoFijoRepository.save(plazoFijo)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlazoFijoResponseDto> obtenerTodos() {
        return plazoFijoRepository.findAll()
                .stream()
                .map(plazoFijoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PlazoFijoResponseDto obtenerPorId(Long id) {
        PlazoFijo plazoFijo = plazoFijoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el plazo fijo con id: " + id
                ));

        return plazoFijoMapper.toResponse(plazoFijo);
    }

    @Override
    public void procesarVencimientos() {
        LocalDate fechaActual = LocalDate.now();

        List<PlazoFijo> plazosVencidos =
                plazoFijoRepository.findByEstadoAndFechaVencimientoLessThanEqual(
                        EstadoPlazoFijo.ACTIVO,
                        fechaActual
                );

        for (PlazoFijo plazoFijo : plazosVencidos) {
            AcreditarFondosRequestDto acreditacion =
                    new AcreditarFondosRequestDto();

            acreditacion.setMonto(plazoFijo.getMontoAlVencimiento());

            cuentaClient.acreditarFondos(
                    plazoFijo.getCuentaId(),
                    acreditacion
            );

            plazoFijo.setEstado(EstadoPlazoFijo.VENCIDO);
            plazoFijoRepository.save(plazoFijo);
        }
    }

    private BigDecimal calcularInteres(BigDecimal monto, Integer plazoDias) {
        return monto
                .multiply(tna)
                .multiply(BigDecimal.valueOf(plazoDias))
                .divide(DIAS_DEL_ANIO, ESCALA_MONETARIA, RoundingMode.HALF_UP);
    }

    @Override
    public List<PlazoFijoResponseDto> obtenerPorCuentaId(Long cuentaId) {
        return plazoFijoRepository.obtenerPorCuentaId(cuentaId)
                .stream()
                .map(plazoFijoMapper::toResponse)
                .toList();
    }
}