package io.github.panjung99.panapi.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ModelType {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String iconUrl;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}