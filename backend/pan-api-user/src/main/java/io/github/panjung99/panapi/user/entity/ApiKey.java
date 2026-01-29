package io.github.panjung99.panapi.user.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class ApiKey {
    private Long id;
    private Long userId;
    private String keyName;
    private String apiKey;
    private Boolean deleted;
    private LocalDateTime createTime;
}