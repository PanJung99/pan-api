package io.github.panjung99.panapi.model.entity;

import io.github.panjung99.panapi.common.enums.ModelCategory;
import io.github.panjung99.panapi.common.enums.PlatformTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 模型标签表实体类
 */
@Data
public class Model {
    /**
     * 标签ID(系统内部标识)
     */
    private Long id;

    /**
     * API调用的标识名称（唯一）
     */
    private String name;

    /**
     * 对外展示的标签名称
     */
    private String displayName;

    /**
     * 是否免费
     */
    private Boolean isFree = false;

    /**
     * chat,image,audio,video,embedding
     * 对话模型，绘画模型，语音模型，视频模型，多模态向量模型
     */
    private ModelCategory category;

    /**
     * 模型厂商：deepseek、chatgpt、glm等等
     */
    private PlatformTypeEnum platformType;

    /**
     * 模型描述
     */
    private String description;

    /**
     * 是否激活：0-禁用 1-启用
     */
    private Integer isActive;

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
