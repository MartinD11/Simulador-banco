package com.tubanco.msnotificaciones.consumer;

import com.tubanco.msnotificaciones.dto.response.TransaccionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacionConsumer {

    // Inyectamos el servicio oficial de Spring para el envio de correos
    private final JavaMailSender mailSender;

    // Indicamos que este metodo vigila de manera permanente la cola de transacciones
    @RabbitListener(queues = "cola.transacciones")
    public void enviarCorreoNotificacion(TransaccionResponseDto transaccionDto) {
        log.info("📧 [EVENTO RECIBIDO EN NOTIFICACIONES] Procesando correo para la transacción ID: {}", transaccionDto.getId());

        try {
            String emailDestino = transaccionDto.getEmailCliente();
            if (emailDestino == null || emailDestino.isBlank()) {
                log.warn("⚠️ [EMAIL OMITIDO] La transacción ID: {} no posee email del cliente.", transaccionDto.getId());
                return;
            }

            // Creamos un mensaje de correo plano
            SimpleMailMessage mensaje = new SimpleMailMessage();

            // Destinatario: extraído dinámicamente del DTO
            mensaje.setTo(emailDestino);

            // Asunto del correo dinámico usando el ID de la transacción
            mensaje.setSubject("TuBanco - Comprobante de Operación Exitosa #" + transaccionDto.getId());

            String nombreCliente = transaccionDto.getTitularOrigen() != null
                    ? transaccionDto.getTitularOrigen()
                    : (transaccionDto.getTitularDestino() != null ? transaccionDto.getTitularDestino() : "Cliente");

            // Cuerpo del mensaje formateado con los datos reales del DTO
            mensaje.setText(String.format(
                    "¡Hola, %s!\n\n" +
                            "Te informamos que se ha procesado exitosamente una operación en tu cuenta.\n\n" +
                            "Detalles del movimiento:\n" +
                            "- ID de Transacción: %d\n" +
                            "- Tipo de Operación: %s\n" +
                            "- Monto: $%s\n" +
                            "- Fecha y Hora: %s\n\n" +
                            "Gracias por operar con TuBanco.",
                    nombreCliente,
                    transaccionDto.getId(),
                    transaccionDto.getTipo(),
                    transaccionDto.getMonto(),
                    transaccionDto.getFechaTransaccion()
            ));

            // Disparamos el envío real a través de SMTP (Gmail)
            mailSender.send(mensaje);

            log.info("✅ [EMAIL ENVIADO CON ÉXITO] Correo entregado a {} para la transacción ID: {}", emailDestino, transaccionDto.getId());

        } catch (Exception e) {
            log.error("❌ [ERROR CRÍTICO AL ENVIAR MAIL] Ocurrió un fallo en el servidor SMTP: {}", e.getMessage());

            // Lanzar la excepción aquí permite que si configuras la DLQ, el mensaje reintente o viaje a la cola muerta
            throw new RuntimeException("Fallo al enviar correo electrónico", e);
        }
    }
}
