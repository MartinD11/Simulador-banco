package com.tubanco.mspagos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponseDto {

    private Long id;
    private Long cuentaId;
    private BigDecimal monto;
    private String estado;
    private String mercadoPagoPaymentId;
    private String externalReference;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaAcreditacion;
}