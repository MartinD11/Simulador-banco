package com.tubanco.mspagos.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPagoRequestDto {

    @NotNull(message = "El id de la cuenta es obligatorio")
    @Positive(message = "El id de la cuenta debe ser mayor a cero")
    private Long cuentaId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal monto;

    private String descripcion;
}