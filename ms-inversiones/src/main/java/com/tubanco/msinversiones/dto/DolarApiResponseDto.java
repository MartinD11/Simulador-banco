package com.tubanco.msinversiones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DolarApiResponseDto {
    private String moneda;
    private String nombre;
    private BigDecimal compra;
    private BigDecimal venta;
    private LocalDateTime fechaActualizacion;
}
