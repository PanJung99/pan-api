package io.github.panjung99.panapi.common.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "API 订单响应对象")
public class ApiOrderResp {

    @Schema(description = "请求id", example = "2008073209202606080")
    private String reqId;

    @Schema(description = "总 Token 数", example = "1024")
    private Integer totalTokens;

    @Schema(description = "API 调用发起时间", example = "2026-01-05 15:09:30")
    private LocalDateTime callTime;

    @Schema(description = "用户 ID", example = "18")
    private Long userId;

    @Schema(description = "用户实际扣费金额", example = "0.1575000000")
    private BigDecimal amount;

    @Schema(description = "平台向服务商支付的成本金额", example = "0.1150000000")
    private BigDecimal cost;

    @Schema(description = "调用的平台模型标识名称", example = "gpt-image-1")
    private String modelName;

    @Schema(description = "API 请求路径/接口类型", example = "/v1/chat/completions")
    private String apiType;
}