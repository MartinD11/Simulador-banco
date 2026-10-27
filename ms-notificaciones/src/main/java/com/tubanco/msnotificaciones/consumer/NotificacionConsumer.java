package com.tubanco.msnotificaciones.consumer;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tubanco.msnotificaciones.dto.response.TransaccionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacionConsumer {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = "cola.transacciones")
    public void enviarCorreoNotificacion(Message mensajeRabbit) {
        try {
            String jsonBody = new String(mensajeRabbit.getBody(), StandardCharsets.UTF_8);
            log.info("🔍 [DEBUG RABBIT] JSON crudo recibido: {}", jsonBody);

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            // Registramos el módulo de fechas de Java 8 para soportar LocalDateTime
            mapper.registerModule(new JavaTimeModule());

            TransaccionResponseDto transaccionDto = mapper.readValue(jsonBody, TransaccionResponseDto.class);

            log.info("📧 [EVENTO RECIBIDO] Transacción ID: {}, Tipo: {}, Monto: {}",
                    transaccionDto.getId(), transaccionDto.getTipo(), transaccionDto.getMonto());

            String emailDestino = transaccionDto.getEmailCliente();
            log.info("🔍 [DEBUG MAIL] Email extraído del DTO: '{}'", emailDestino);

            if (emailDestino == null || emailDestino.isBlank()) {
                log.warn("⚠️ [EMAIL OMITIDO] La transacción ID: {} NO posee email del cliente o llegó nulo.", transaccionDto.getId());
                return;
            }

            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(emailDestino);
            mensaje.setSubject("TuBanco - Comprobante de Operación Exitosa #" + transaccionDto.getId());

            String nombreCliente = transaccionDto.getTitularOrigen() != null
                    ? transaccionDto.getTitularOrigen()
                    : (transaccionDto.getTitularDestino() != null ? transaccionDto.getTitularDestino() : "Cliente");

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

            log.info("📤 [DEBUG SMTP] Intentando conectar con Gmail para enviar correo a {}...", emailDestino);
            mailSender.send(mensaje);
            log.info("✅ [EMAIL ENVIADO CON ÉXITO] Correo entregado a {}", emailDestino);

        } catch (Exception e) {
            log.error("❌ [ERROR CRÍTICO AL PROCESAR/ENVIAR MAIL]: ", e);
            throw new RuntimeException("Fallo al procesar el mensaje de RabbitMQ", e);
        }
    }
}