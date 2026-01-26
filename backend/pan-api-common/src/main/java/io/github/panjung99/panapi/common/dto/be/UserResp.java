package io.github.panjung99.panapi.common.dto.be;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserResp {

    /**
     * 用户名
     */
    private String username;

    /**
     *
     */
    private String phone;

    /**
     * 邮件
     */
    private String email;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;

}
