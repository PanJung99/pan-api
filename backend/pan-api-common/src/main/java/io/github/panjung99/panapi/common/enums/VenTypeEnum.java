package io.github.panjung99.panapi.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
public enum VenTypeEnum {
    COMMON("COMMON", "通用类型"),
    DEEP_SEEK("DEEP_SEEK", "深度求索"),
    GLM("GLM", "智谱清言"),
    DOU_BAO("DOU_BAO", "字节跳动豆包(火山引擎)"),
    XUN_FEI("XUN_FEI", "讯飞星火")
    ;

    private final String type;
    private final String description;

    VenTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public static VenTypeEnum fromType(String type) {
        for (VenTypeEnum value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
