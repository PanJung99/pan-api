package io.github.panjung99.panapi.user.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Bill {
    private Long id;
    private Long userId;
    private Long apiKeyId;
    private Integer billType; // 1-充值 2-API扣费 99-人工调整
    private BigDecimal amount;
    private BigDecimal beforeBalance;
    private BigDecimal afterBalance;
    private String relatedId;
    private String description;
    private Long modelTagId;
    private String modelTagName;
    private LocalDateTime createTime;

    // 账单类型枚举
    public enum BillType {
        RECHARGE(1), API_DEDUCTION(2), MANUAL_ADJUSTMENT(99);

        private final int code;

        BillType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static BillType fromCode(int code) {
            for (BillType type : BillType.values()) {
                if (type.code == code) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid bill type code: " + code);
        }
    }
}