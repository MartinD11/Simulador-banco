package com.tubanco.mscuentas.dto.response;

import com.tubanco.mscuentas.enums.EstadoTransaccion;
import com.tubanco.mscuentas.enums.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionResponseDto {
    private Long id;
    private Long cuentaOrigenId;
    private Long cuentaDestinoId;
    private String cbuOrigen;
    private String titularOrigen;
    private String cbuDestino;
    private String titularDestino;
    private BigDecimal monto;
    private TipoTransaccion tipo;
    private EstadoTransaccion estado;
    private LocalDateTime fechaTransaccion;
}