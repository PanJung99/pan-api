package io.github.panjung99.panapi.common.dto.be;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GenerationTaskVO {
    private String reqId;
    private String taskType;
    private String resourceUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}