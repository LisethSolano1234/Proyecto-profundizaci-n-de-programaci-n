package com.proyecto.vehiculos.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    // Token de prueba (luego puedes cambiarlo o generarlo dinámicamente)
    private static final String VALID_TOKEN = "TOKEN123";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String token = request.getHeader("Authorization");

        if (token == null || !token.equals("Bearer " + VALID_TOKEN)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(" Acceso no autorizado: token inválido o ausente.");
            return false;
        }

        return true; // Token válido → continuar
    }
}
