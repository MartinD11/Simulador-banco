package com.tubanco.mspagos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPagoResponseDto {

    private String preferenceId;
    private String initPoint;
    private String sandboxInitPoint;
    private String externalReference;
}