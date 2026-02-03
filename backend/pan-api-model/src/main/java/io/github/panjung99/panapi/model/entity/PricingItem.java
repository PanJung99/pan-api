package io.github.panjung99.panapi.model.entity;

import io.github.panjung99.panapi.common.enums.UnitEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PricingItem {
    private Long id;

    /**
     * 平台模型ID
     */
    private Long modelId;

    /**
     * 计费单位: mtokens, nums 等
     */
    private UnitEnum unit;

    /**
     * 输入单价
     */
    private BigDecimal priceInput;

    /**
     * 输出单价
     */
    private BigDecimal priceOutput;

    /**
     * 货币类型: CNY, USD
     */
    private String currency;

    /**
     * 是否激活：0-禁用 1-启用
     */
    private Integer isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
