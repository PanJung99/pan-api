package io.github.panjung99.panapi.common.dto.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VendorTokenReq {
    private Long id;           // Token ID
    private Long vendorId;     // 服务商ID
    private String apiKey;     // API密钥
    private String tokenName;  // Token名称
    private Boolean isActive;  // 是否启用
}