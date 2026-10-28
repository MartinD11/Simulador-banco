package com.tubanco.msinversiones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
public class MsInversionesApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        SpringApplication.run(MsInversionesApplication.class, args);
    }

}
