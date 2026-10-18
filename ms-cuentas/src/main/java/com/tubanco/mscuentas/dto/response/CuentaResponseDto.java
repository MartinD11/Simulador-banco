package com.tubanco.mscuentas.dto.response;

import com.tubanco.mscuentas.entity.Tarjeta;
import com.tubanco.mscuentas.enums.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuentaResponseDto {
    private Long id;
    private String cbu;
    private BigDecimal saldo;
    private TipoCuenta tipoCuenta;
    private TarjetaResponseDto tarjeta;
}
