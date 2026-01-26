package io.github.panjung99.panapi.common.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VendorTokenCreateReq {

    @NotBlank(message = "apiKey不能为空")
    private String apiKey;

    @NotBlank(message = "tokenName不能为空")
    private String tokenName;

    private Long isActive;

    private LocalDateTime expiresAt;
}