package com.tubanco.mscuentas.dto.request;

import com.tubanco.mscuentas.enums.TipoCuenta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Objeto que representa los datos necesarios para crear una nueva cuenta bancaria")
public class CrearCuentaRequestDto {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Schema(description = "ID único del usuario dueño de la cuenta", example = "1045", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long usuarioId;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    @Schema(description = "El tipo de cuenta que se desea abrir (ej. CAJA_AHORRO, CUENTA_CORRIENTE)", example = "CAJA_AHORRO", requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoCuenta tipoCuenta;

}