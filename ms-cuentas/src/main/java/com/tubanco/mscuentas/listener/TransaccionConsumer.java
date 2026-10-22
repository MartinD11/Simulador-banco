package com.tubanco.mscuentas.listener;

import com.tubanco.mscuentas.config.RabbitMQConfig;
import com.tubanco.mscuentas.dto.response.TransaccionResponseDto; // O un DTO equivalente que coincida con lo que envías
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransaccionConsumer {

    // La anotación @RabbitListener se queda vigilando la cola de forma permanente
    @RabbitListener(queues = RabbitMQConfig.COLA_TRANSACCIONES)
    public void consumirTransaccion(TransaccionResponseDto transaccionDto) {
        log.info("📥 [EVENTO RECIBIDO] Transacción ID: {} procesada por un monto de: {}",
                transaccionDto.getId(), transaccionDto.getMonto());

        // Acá posteriormente podés disparar la lógica para actualizar saldos o registrar auditorías en ms-cuentas
    }
}