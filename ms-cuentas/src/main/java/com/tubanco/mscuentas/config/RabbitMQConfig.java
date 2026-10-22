package com.tubanco.mscuentas.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String COLA_TRANSACCIONES = "cola.transacciones";

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Configuramos el conversor seguro para deserializar el JSON entrante
        return new JacksonJsonMessageConverter("com.tubanco");
    }

    @Bean
    public Queue colaTransacciones() {
        return QueueBuilder.durable(COLA_TRANSACCIONES)
                .withArgument("x-dead-letter-exchange", "exchange.dlx") // Si falla, va a este exchange alternativo
                .withArgument("x-dead-letter-routing-key", "routing.key.dlq") // Con esta etiqueta
                .build();
    }

    @Bean
    public Queue colaMuertaTransacciones() {
        return QueueBuilder.durable("cola.transacciones.dlq").build();
    }

    @Bean
    public TopicExchange exchangeDlq() {
        return new TopicExchange("exchange.dlx");
    }

    @Bean
    public Binding bindingDlq(Queue colaMuertaTransacciones, TopicExchange exchangeDlq) {
        return BindingBuilder.bind(colaMuertaTransacciones).to(exchangeDlq).with("routing.key.dlq");
    }
}