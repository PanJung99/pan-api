package io.github.panjung99.panapi.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private Integer loginType;

    // 密码相关
    private String password;

    // 多种登录方式
    private String wechatOpenid;
    private String phone;
    private String email;

    private Boolean deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 登录类型枚举
    public enum LoginType {
        PASSWORD(1), WECHAT(2), PHONE(3);

        private final int code;

        LoginType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static LoginType fromCode(int code) {
            for (LoginType type : LoginType.values()) {
                if (type.code == code) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid login type: " + code);
        }
    }
}