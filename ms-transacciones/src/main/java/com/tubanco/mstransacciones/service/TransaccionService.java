package com.tubanco.mstransacciones.service;

import java.math.BigDecimal;

public interface TransaccionService {
    void procesarTransaccion(Long cuentaId, Long tipoTransaccion, BigDecimal monto);

}
