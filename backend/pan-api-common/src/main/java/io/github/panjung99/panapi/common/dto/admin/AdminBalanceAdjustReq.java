package io.github.panjung99.panapi.common.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "用户余额调整请求")
public class AdminBalanceAdjustReq {

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", required = true, example = "1")
    private Long userId;

    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    @Schema(description = "调整金额（正数表示增加，负数表示减少）", required = true, example = "100.00")
    private BigDecimal amount;

    @Schema(description = "调整原因", example = "人工充值")
    private String reason;
}
