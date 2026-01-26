package io.github.panjung99.panapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum PlatformEnum {
    DEEP_SEEK("deepseek", "deepseek", "123132", "deepseek");

    private final String code;
    private final String name;
    private final String description;
    private final String iconUrl;

    PlatformEnum(String code, String name, String description, String iconUrl) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
    }
}
