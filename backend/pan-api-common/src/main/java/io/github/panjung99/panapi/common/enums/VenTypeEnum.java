package io.github.panjung99.panapi.common.enums;

import lombok.Getter;

@Getter
public enum VenTypeEnum {
    COMMON("通用类型", null),
    OPEN_AI("OpenAI", null),
    DEEP_SEEK("深度求索", "https://api.deepseek.com/v1"),
    GLM("智谱清言", null),
    DOU_BAO("豆包(火山引擎)", "https://ark.cn-beijing.volces.com/api/v3"),
    XUN_FEI("讯飞星火", null),
    GEMINI("Google Gemini", null)
    ;

    private final String name;
    private final String apiBaseUrl;

    VenTypeEnum(String name, String apiBaseUrl) {
        this.name = name;
        this.apiBaseUrl = apiBaseUrl;
    }
}
