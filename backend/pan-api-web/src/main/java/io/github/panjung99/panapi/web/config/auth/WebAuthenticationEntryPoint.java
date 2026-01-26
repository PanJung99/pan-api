package io.github.panjung99.panapi.web.config.auth;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class WebAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.debug("认证失败，路径：{}，原因：{}", request.getRequestURI(), authException.getMessage(), authException);

        response.setStatus(ErrorEnum.UNAUTHORIZED_ERROR.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(ErrorEnum.UNAUTHORIZED_ERROR.toJsonString());
    }
}