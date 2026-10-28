package com.tubanco.msinversiones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient dolarRestClient(
            RestClient.Builder builder,
            @Value("${dolar.api.base-url}") String baseUrl) {

        return builder
                .baseUrl(baseUrl)
                .build();
    }
}