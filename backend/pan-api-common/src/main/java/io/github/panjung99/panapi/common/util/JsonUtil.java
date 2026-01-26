package io.github.panjung99.panapi.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {
    // 线程安全的 ObjectMapper (推荐使用 Jackson 2.10+)
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * TODO obj为null时会报错吗
     * @param obj
     * @return
     */
    // 静态序列化方法
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("toJson error{}", e.getMessage(), e);
            throw new RuntimeException("JSON serialization failed", e);
        }
    }
}