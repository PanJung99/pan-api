package io.github.panjung99.panapi.common.enums;

import lombok.Getter;

@Getter
public enum UnitEnum {
    mtokens,   // 百万 tokens
    mchars,    // 百万字符
    times,     // 次数
    nums,      // 数量
    minutes,   // 分钟
    seconds,    // 秒
    none; //空值


    public static UnitEnum fromCode(String unit) {
        for (UnitEnum value : values()) {
            if (value.name().equals(unit)) {
                return value;
            }
        }
        return none;
    }
}