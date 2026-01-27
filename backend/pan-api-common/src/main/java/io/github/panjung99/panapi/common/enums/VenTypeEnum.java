package io.github.panjung99.panapi.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
public enum VenTypeEnum {
    COMMON("通用类型"),
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
