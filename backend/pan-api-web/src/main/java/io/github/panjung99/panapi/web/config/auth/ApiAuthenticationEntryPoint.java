package io.github.panjung99.panapi.web.config.auth;

import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(ErrorEnum.API_INVALID_AUTH_HEADER.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(ErrorEnum.API_INVALID_AUTH_HEADER.toOpenAIJsonString());
    }
}