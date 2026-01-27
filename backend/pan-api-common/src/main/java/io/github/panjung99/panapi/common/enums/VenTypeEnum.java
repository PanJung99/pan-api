package io.github.panjung99.panapi.common.enums;

import lombok.Getter;

@Getter
public enum VenTypeEnum {
    COMMON("通用类型"),
    OPEN_AI("OpenAI"),
    DEEP_SEEK("深度求索"),
    GLM("智谱清言"),
    DOU_BAO("字节跳动豆包(火山引擎)"),
    XUN_FEI("讯飞星火")
    ;

    private final String description;

    VenTypeEnum(String description) {
        this.description = description;
    }
}
