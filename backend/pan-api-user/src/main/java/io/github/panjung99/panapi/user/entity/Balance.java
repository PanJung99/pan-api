package io.github.panjung99.panapi.user.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Balance {
    private Long id;
    private Long userId;
    private BigDecimal currentBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}