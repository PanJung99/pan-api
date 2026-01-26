package io.github.panjung99.panapi.common.dto.be;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginReq {

    @NotBlank(message = "账号/手机号不能为空")
    private String identifier; // 可以是用户名、手机号或邮箱

    @NotBlank(message = "密码不能为空")
    private String password;
}
