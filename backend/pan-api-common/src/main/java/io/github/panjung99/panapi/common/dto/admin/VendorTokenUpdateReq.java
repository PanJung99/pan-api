package io.github.panjung99.panapi.common.dto.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VendorTokenUpdateReq {

    private String apiKey;

    private String tokenName;

    private Long isActive;

    private LocalDateTime expiresAt;
}