package com.tubanco.mstransacciones.service.serviceimpl;

import com.tubanco.mstransacciones.repository.TransaccionRepository;
import com.tubanco.mstransacciones.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransaccionServiceImpl implements TransaccionService {
    private final TransaccionRepository transaccionRepository;

    @Override
    public void procesarTransaccion(Long cuentaId, Long tipoTransaccion, BigDecimal monto) {

    }
}
