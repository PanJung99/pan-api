package io.github.panjung99.panapi.common.dto.be;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "用户注册请求")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterReq {

    @Schema(description = "用户名", example = "example_username", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度4-20位")
    private String username;

    @Schema(description = "密码", example = "example_password", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 30, message = "密码长度8-30位")
    private String password;

    @Schema(description = "邮箱", example = "example@panapi.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "邮箱格式不正确")
    private String email;
}