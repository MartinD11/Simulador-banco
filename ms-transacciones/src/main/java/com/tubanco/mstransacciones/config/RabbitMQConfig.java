package com.tubanco.mstransacciones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String COLA_TRANSACCIONES = "cola.transacciones";
    public static final String EXCHANGE_TRANSACCIONES = "exchange.transacciones";
    public static final String ROUTING_KEY_TRANSACCIONES = "routing.key.transacciones";

    @Bean
    public Queue colaTransacciones() {
        return new Queue(COLA_TRANSACCIONES, true);
    }

    @Bean
    public TopicExchange exchangeTransacciones() {
        return new TopicExchange(EXCHANGE_TRANSACCIONES);
    }

    @Bean
    public Binding bindingTransacciones(Queue colaTransacciones, TopicExchange exchangeTransacciones) {
        return BindingBuilder.bind(colaTransacciones).to(exchangeTransacciones).with(ROUTING_KEY_TRANSACCIONES);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Conversor oficial nativo moderno para Spring AMQP 4.x / Spring Boot 3+
        return new JacksonJsonMessageConverter("com.tubanco");
    }
}