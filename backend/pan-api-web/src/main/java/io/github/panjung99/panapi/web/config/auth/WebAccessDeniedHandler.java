package io.github.panjung99.panapi.web.config.auth;

import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class WebAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.info("Access denied reqUrl:{} reqBody:{}", request.getRequestURI(), request.getReader().readLine());
        response.setStatus(ErrorEnum.ACCESS_DENIED.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(ErrorEnum.ACCESS_DENIED.toJsonString());
    }
}
