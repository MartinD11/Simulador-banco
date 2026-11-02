package com.tubanco.mspagos.dto.mercadopago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPreferenciaRequestDto {

    private List<ItemDto> items;

    private String externalReference;

    private BackUrlsDto backUrls;

    private String notificationUrl;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDto {

        private String title;
        private String description;
        private Integer quantity;
        private String currencyId;
        private BigDecimal unitPrice;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BackUrlsDto {

        private String success;
        private String pending;
        private String failure;
    }
}