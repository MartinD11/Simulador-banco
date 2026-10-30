package com.tubanco.msinversiones.client;

import com.tubanco.msinversiones.dto.request.DebitarFondosRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-cuentas")
public interface CuentaClient {

    @PutMapping("/api/cuentas/{id}/debitar")
    void debitarFondos(
            @PathVariable("id") Long cuentaId,
            @RequestBody DebitarFondosRequestDto request);
}