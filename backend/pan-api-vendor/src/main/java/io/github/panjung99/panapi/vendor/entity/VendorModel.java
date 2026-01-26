package io.github.panjung99.panapi.vendor.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务商模型表实体类
 */
@Data
public class VendorModel {

    /**
     * 模型ID
     */
    private Long id;

    /**
     * 服务商ID
     */
    private Long vendorId;

    /**
     * 模型名称(服务商内部名称)
     */
    private String name;

    /**
     * 逻辑删除：0-正常 1-已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
