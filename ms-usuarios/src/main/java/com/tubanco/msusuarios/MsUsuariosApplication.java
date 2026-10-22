package com.tubanco.msusuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.TimeZone;
@SpringBootApplication(scanBasePackages = {"com.tubanco.msusuarios", "com.tubanco.bancocommon"})
public class MsUsuariosApplication {

    public static void main(String[] args) {
        // Neutraliza el error de timezone de Windows antes de conectarse a la base de datos
        TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));

        SpringApplication.run(MsUsuariosApplication.class, args);
    }

}
