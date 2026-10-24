package com.tubanco.mstransacciones.dto.response;

import com.tubanco.mstransacciones.enums.EstadoCuenta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaResponseDto {
    private Long id;
    private Long usuarioId;
    private String cbu;
    private BigDecimal saldo;
    private EstadoCuenta estado;
    private String titular;
    private String email;
}
