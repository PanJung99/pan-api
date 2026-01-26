package io.github.panjung99.panapi.web.config.rate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebRateLimitConfig implements WebMvcConfigurer {

    @Autowired
    private APIRateLimiterInterceptor apiRateLimiterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiRateLimiterInterceptor)
                .addPathPatterns("/api/**");       // 拦截 /api 下所有请求
//                .excludePathPatterns("/be/api/query"); // 排除 /be/api/query
    }
}
