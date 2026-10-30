package com.tubanco.msinversiones.scheduler;

import com.tubanco.msinversiones.service.PlazoFijoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlazoFijoScheduler {

    private final PlazoFijoService plazoFijoService;

    @Scheduled(
            cron = "0 0 0 * * *",
            zone = "America/Argentina/Buenos_Aires"
    )
    public void procesarVencimientos() {
        log.info("Iniciando proceso de vencimiento de plazos fijos");
        plazoFijoService.procesarVencimientos();
        log.info("Proceso de vencimiento finalizado");
    }
}