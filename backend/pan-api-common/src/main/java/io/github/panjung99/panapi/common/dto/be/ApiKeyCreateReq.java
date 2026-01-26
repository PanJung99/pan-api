package io.github.panjung99.panapi.common.dto.be;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "API Key创建请求")
@Data
public class ApiKeyCreateReq {

    @Schema(description = "API Key名称", example = "test api key", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "名称不能为空")
    private String keyName;

    @Schema(description = "限额", example = "100.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal quota;

    @Schema(description = "过期时间", example = "2026-01-08T00:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime expireTime;


}
