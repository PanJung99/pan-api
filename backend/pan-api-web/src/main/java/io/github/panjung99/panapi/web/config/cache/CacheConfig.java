package io.github.panjung99.panapi.web.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching // 开启缓存支持
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // 配置Caffeine参数
        cacheManager.setCaffeine(Caffeine.newBuilder()
                        .initialCapacity(100) // 初始容量
                        .maximumSize(2000) // 最大缓存条数
                        .expireAfterWrite(60, TimeUnit.SECONDS) // 写入后60秒过期
                        .expireAfterAccess(600, TimeUnit.SECONDS) // 访问后600秒过期
        );
        return cacheManager;
    }

    @Bean("tenantAwareKeyGenerator")
    @Primary  // 这里设置为主要KeyGenerator
    public KeyGenerator tenantAwareKeyGenerator() {
        return new MyKeyGenerator();
    }
}