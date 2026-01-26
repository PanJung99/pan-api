package io.github.panjung99.panapi.common.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class Md5Util {

    /**
     * 生成字符串的大写32位MD5值
     * @param input 原始字符串
     * @return 大写的32位MD5字符串
     */
    public static String getMd5Upper(String input) {
        if (input == null) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(input.getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }

}