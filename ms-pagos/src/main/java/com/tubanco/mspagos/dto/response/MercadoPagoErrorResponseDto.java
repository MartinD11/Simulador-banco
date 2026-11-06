package com.tubanco.mspagos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MercadoPagoErrorResponseDto {

    private String message;
    private String error;
    private Integer status;
    private List<MercadoPagoErrorCauseDto> cause;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MercadoPagoErrorCauseDto {

        private String code;
        private String description;
        private String data;
    }
}
