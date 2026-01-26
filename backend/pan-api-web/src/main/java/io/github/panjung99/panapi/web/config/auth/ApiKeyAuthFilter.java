package io.github.panjung99.panapi.web.config.auth;

import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.user.entity.ApiKey;
import io.github.panjung99.panapi.user.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private ApiKeyService apiKeyService;

    public ApiKeyAuthFilter(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String token = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                ApiKey apiKey = apiKeyService.getByApiKey(token);
                if (apiKey == null) {
                    throw new AppException(ErrorEnum.API_AUTHENTICATION_ERROR);
                }
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        apiKey, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (AppException e) {
            // 不做处理，交给EntryPoint处理
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
