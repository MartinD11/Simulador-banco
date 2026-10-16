package com.tubanco.mscuentas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TarjetaResponseDto {
    private String numeroTarjeta;
    private LocalDate fechaVencimiento;
}