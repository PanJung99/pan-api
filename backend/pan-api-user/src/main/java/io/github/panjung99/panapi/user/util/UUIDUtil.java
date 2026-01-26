package io.github.panjung99.panapi.user.util;

import java.util.UUID;

public class UUIDUtil {
    /**
     * 生成标准UUID字符串（无中划线）
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成带中划线的标准UUID字符串
     */
    public static String randomUUIDWithDash() {
        return UUID.randomUUID().toString();
    }

} 