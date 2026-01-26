package io.github.panjung99.panapi.vendor.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VendorToken {
    private Long id;
    private Long vendorId;
    private String apiKey;
    private String tokenName;
    private Long isActive;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}