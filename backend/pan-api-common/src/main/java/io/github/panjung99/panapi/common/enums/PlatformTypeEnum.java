package io.github.panjung99.panapi.common.enums;

import lombok.Getter;

@Getter
public enum PlatformTypeEnum {
    DEEP_SEEK("深度求索", "123132", "deepseek"),
    OPEN_AI("OpenAI", "", "");

    private final String name;
    private final String description;
    private final String iconUrl;

    PlatformTypeEnum(String name, String description, String iconUrl) {
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
    }
}
