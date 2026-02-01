package io.github.panjung99.panapi.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelBinding {

    private Long id;

    /** 平台模型ID */
    private Long modelId;

    /** 服务商模型ID */
    private Long venModelId;

    /** 是否启用：0-禁用 1-启用 */
    private Integer isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
