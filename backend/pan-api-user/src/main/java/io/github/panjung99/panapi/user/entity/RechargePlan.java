package io.github.panjung99.panapi.user.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RechargePlan {
    private Long id;
    private String name;
    private BigDecimal price;
    private String desc;
    private String displayMd;
    private String productCode;
    private Integer isRecommend;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}