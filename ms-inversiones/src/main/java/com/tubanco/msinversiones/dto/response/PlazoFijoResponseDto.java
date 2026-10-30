package com.tubanco.msinversiones.dto.response;

import com.tubanco.msinversiones.enums.EstadoPlazoFijo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlazoFijoResponseDto {

    private Long id;
    private Long cuentaId;
    private BigDecimal monto;
    private BigDecimal tna;
    private Integer plazoDias;
    private BigDecimal interesGenerado;
    private BigDecimal montoAlVencimiento;
    private LocalDate fechaConstitucion;
    private LocalDate fechaVencimiento;
    private EstadoPlazoFijo estado;
}