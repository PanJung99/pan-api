package io.github.panjung99.panapi.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Admin {
    private Long id;
    private Long tenantId;
    private Long userId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}