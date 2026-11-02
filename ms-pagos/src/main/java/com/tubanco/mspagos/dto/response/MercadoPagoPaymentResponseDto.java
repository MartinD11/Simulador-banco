package com.tubanco.mspagos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MercadoPagoPaymentResponseDto {

    private Long id;

    private String status;

    private String statusDetail;

    private BigDecimal transactionAmount;

    private BigDecimal netReceivedAmount;

    private String externalReference;

    private String paymentMethodId;

    private String paymentTypeId;

    private OffsetDateTime dateApproved;
}