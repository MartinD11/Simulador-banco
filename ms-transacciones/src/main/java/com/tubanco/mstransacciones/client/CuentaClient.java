package com.tubanco.mstransacciones.client;

import com.tubanco.mstransacciones.dto.response.CuentaResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "ms-cuentas")
public interface CuentaClient {

    @GetMapping("/api/cuentas/{id}")
    CuentaResponseDto obtenerCuentaPorId(@PathVariable("id") Long id);

    @PutMapping("/api/cuentas/{id}/actualizar-saldo")
    void actualizarSaldo(@PathVariable("id") Long id, @RequestParam("monto") BigDecimal monto);
}


