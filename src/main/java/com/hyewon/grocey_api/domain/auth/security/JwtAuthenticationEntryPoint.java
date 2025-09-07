package com.hyewon.grocey_api.domain.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String exception = (String) request.getAttribute("exception");

        if ("expired".equals(exception)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired"); // 401
        } else if ("invalid".equals(exception)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token"); // 401
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");  // 기본
        }
    }
}
