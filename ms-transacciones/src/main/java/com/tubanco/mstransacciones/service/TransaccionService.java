package com.tubanco.mstransacciones.service;

import java.math.BigDecimal;

public interface TransaccionService {
    Long procesarTransaccion(Long cuentaId, BigDecimal monto,Long destinoId);

}
