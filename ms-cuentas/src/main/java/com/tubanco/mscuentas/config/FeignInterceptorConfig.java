package com.tubanco.mscuentas.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignInterceptorConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        //Sello de llamada interna
        template.header("X-Llamada-Interna", "true");

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            String usuarioId = request.getHeader("X-Usuario-Id");
            String rol = request.getHeader("X-Usuario-Rol");

            // Rescatamos el Token JWT de la petición original
            String authorization = request.getHeader("Authorization");

            if (usuarioId != null) {
                template.header("X-Usuario-Id", usuarioId);
            }
            if (rol != null) {
                template.header("X-Usuario-Rol", rol);
            }
            //Se lo inyectamos al Feign para que ms-usuarios lo deje pasar
            if (authorization != null) {
                template.header("Authorization", authorization);
            }
        }
    }
}