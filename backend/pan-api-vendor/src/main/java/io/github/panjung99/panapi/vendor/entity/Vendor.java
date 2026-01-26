package io.github.panjung99.panapi.vendor.entity;

import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务商基本信息表实体类
 */
@Data
@Builder
public class Vendor {

    /**
     * 服务商ID
     */
    private Long id;

    /**
     * 服务商名称
     */
    private String name;

    /**
     * 服务商类型
     */
    private VenTypeEnum venType;

    /**
     * API基础地址
     */
    private String apiBaseUrl;

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