package io.github.panjung99.panapi.web.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Feign OkHttp 配置类
 * 显式配置 OkHttpClient 以确保 Feign 使用 OkHttp
 * 
 * 注意：在 Spring Cloud OpenFeign 中，如果配置了 feign.okhttp.enabled=true，
 * 系统会自动配置 OkHttpClient 和 Feign Client。这里只提供自定义的 OkHttpClient Bean，
 * 让 Spring Cloud 自动配置来创建 Feign Client。
 */
@Slf4j
@Configuration
@ConditionalOnClass(OkHttpClient.class)
@ConditionalOnProperty(value = "feign.okhttp.enabled", havingValue = "true", matchIfMissing = false)
public class FeignOkHttpConfig {

    /**
     * 配置 OkHttpClient Bean
     * 使用 @ConditionalOnMissingBean 确保如果 Spring Cloud 已经自动配置了 OkHttpClient，
     * 则不会覆盖它；如果没有自动配置，则使用此 Bean
     * Spring Cloud OpenFeign 会自动检测并使用这个 Bean 来创建 Feign Client
     */
    @Bean
    @ConditionalOnMissingBean(OkHttpClient.class)
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(1200, TimeUnit.SECONDS)
                .followRedirects(true)
                .retryOnConnectionFailure(true)
                .build();
    }
}

