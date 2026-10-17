package com.tubanco.mstransacciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {"com.tubanco.mstransacciones", "com.tubanco.bancocommon"})
@EnableDiscoveryClient
@EnableFeignClients
public class MsTransaccionesApplication {

    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));

        SpringApplication.run(MsTransaccionesApplication.class, args);
    }
}