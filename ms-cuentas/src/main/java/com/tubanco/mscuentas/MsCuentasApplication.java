package com.tubanco.mscuentas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {"com.tubanco.mscuentas", "com.tubanco.bancocommon"})
@EnableFeignClients
public class MsCuentasApplication {

    public static void main(String[] args) {
        // Esto neutraliza el error de Windows antes de conectarse a la base de datos
        TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));

        SpringApplication.run(MsCuentasApplication.class, args);
    }
}