package io.github.panjung99.panapi.web.config;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        // BigDecimal 用字符串序列化，避免科学计数法
        simpleModule.addSerializer(BigDecimal.class, new JsonSerializer<>() {
            @Override
            public void serialize(BigDecimal value, com.fasterxml.jackson.core.JsonGenerator gen,
                                  com.fasterxml.jackson.databind.SerializerProvider serializers) throws java.io.IOException {
                gen.writeNumber(value.toPlainString());
            }
        });
        mapper.registerModule(simpleModule);

        // LocalDateTime 格式化
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATETIME_FORMATTER));
        mapper.registerModule(javaTimeModule);
        return mapper;
    }
}