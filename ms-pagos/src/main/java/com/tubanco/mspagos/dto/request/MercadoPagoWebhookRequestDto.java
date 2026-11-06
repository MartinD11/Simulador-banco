package com.tubanco.mspagos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MercadoPagoWebhookRequestDto {

    private String action;
    private String apiVersion;
    private String applicationId;
    private String dateCreated;
    private String id;
    private Boolean liveMode;
    private String type;
    private WebhookDataDto data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WebhookDataDto {

        private String id;
    }
}