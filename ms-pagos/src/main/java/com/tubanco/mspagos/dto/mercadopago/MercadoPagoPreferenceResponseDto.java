package com.tubanco.mspagos.dto.mercadopago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MercadoPagoPreferenceResponseDto {

    private String id;
    private String initPoint;
    private String sandboxInitPoint;
    private String externalReference;
}
