package io.github.panjung99.panapi.common.dto.be;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Api Key 响应对象")
public class ApiKeyResp {

    @Schema(description = "Api Key ID", example = "11")
    private Long id;

    @Schema(description = "Api Key 名称", example = "TEST KEY")
    private String keyName;

    @Schema(description = "Api Key Token", example = "07471tyc849u23382d9b07539741cfe4")
    private String apiKey;

    @Schema(description = "创建时间", example = "2025-11-24 18:32:54")
    private LocalDateTime createTime;
} 