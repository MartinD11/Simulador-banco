package com.tubanco.msnotificaciones.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransaccionResponseDto {
    private Long id;
    private Long cuentaOrigenId;
    private String cbuOrigen;
    private String titularOrigen;
    private Long cuentaDestinoId;
    private String cbuDestino;
    private String titularDestino;
    private BigDecimal monto;
    private String tipo;
    private String estado;
    private LocalDateTime fechaTransaccion;
    private String emailCliente;
}