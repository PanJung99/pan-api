package io.github.panjung99.panapi.web.config.rate;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class APIRateLimiterInterceptor implements HandlerInterceptor {

    // 每秒最多 100 个请求
    private final RateLimiter rateLimiter = RateLimiter.create(100);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!rateLimiter.tryAcquire()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests for this controller.");
            return false;
        }
        return true;
    }
}