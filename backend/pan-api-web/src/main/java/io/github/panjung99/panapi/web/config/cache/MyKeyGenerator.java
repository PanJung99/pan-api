package io.github.panjung99.panapi.web.config.cache;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;


public class MyKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        // 构建包含租户ID的缓存Key
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(method.getDeclaringClass().getSimpleName()).append(".");
        keyBuilder.append(method.getName());

        // 添加方法参数
        if (params.length > 0) {
            keyBuilder.append(":");
            for (Object param : params) {
                if (param != null) {
                    keyBuilder.append(param).append("_");
                }
            }
            // 删除最后一个下划线
            if (keyBuilder.charAt(keyBuilder.length() - 1) == '_') {
                keyBuilder.deleteCharAt(keyBuilder.length() - 1);
            }
        }

        return keyBuilder.toString();
    }

}