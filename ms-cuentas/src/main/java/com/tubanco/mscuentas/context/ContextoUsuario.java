package com.tubanco.mscuentas.context;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class ContextoUsuario {

    public Long getUsuarioId() {
        String id = getHeader("X-Usuario-Id");
        if (id == null || id.isBlank()) {
            return null;
        }
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getRol() {
        return getHeader("X-Usuario-Rol");
    }

    public boolean isAdmin() {
        String rol = getRol();
        return "ADMIN".equals(rol) || "ROLE_ADMIN".equals(rol);
    }

    public boolean isLlamadaInterna() {
        return "true".equals(getHeader("X-Llamada-Interna"));
    }

    private String getHeader(String headerName) {
        HttpServletRequest request = getRequest();
        return request != null ? request.getHeader(headerName) : null;
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}
