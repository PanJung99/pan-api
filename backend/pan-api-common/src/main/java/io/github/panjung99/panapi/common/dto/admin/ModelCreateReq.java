package io.github.panjung99.panapi.common.dto.admin;

import io.github.panjung99.panapi.common.enums.ModelCategory;
import io.github.panjung99.panapi.common.enums.PlatformTypeEnum;
import io.github.panjung99.panapi.common.enums.UnitEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "创建模型请求对象")
public class ModelCreateReq {

    @Schema(description = "模型名称(唯一标识)", example = "deepseek-chat")
    @NotBlank(message = "模型名称不能为空")
    private String name;

    @Schema(description = "模型展示名称", example = "DeepSeek-V3.2")
    @NotBlank(message = "模型展示名称不能为空")
    private String displayName;

    @Schema(description = "是否免费", example = "false")
    @NotNull(message = "是否免费不能为空")
    private Boolean isFree;

    @Schema(description = "模型种类", example = "chat")
    @NotNull(message = "模型种类不能为空")
    private ModelCategory category;

    @Schema(description = "模型厂商", example = "DEEP_SEEK")
    @NotNull(message = "模型厂商不能为空")
    private PlatformTypeEnum platformType;

    @Schema(description = "模型描述", example = "DeepSeek推出的首个将思考融入工具使用的模型，并且同时支持思考模式与非思考模式的工具调用，智能水平与 GPT-5 相当")
    private String description;

    @Schema(description = "绑定的服务商模型(Vendor Model)ID列表")
    @NotEmpty(message = "必须至少绑定一个服务商模型")
    private List<Long> vendorModelIds;

    @Valid
    @Schema(description = "计费项配置列表 (当 isFree 为 false 时必填)")
    private List<PricingItemCreateReq> pricingItems;

    /**
     * If model not free, then pricingItems must not empty.
     */
    @AssertTrue(message = "非免费模型必须配置计费项")
    @Schema(hidden = true)
    public boolean isPricingItemsValid() {
        if (Boolean.FALSE.equals(isFree)) {
            return pricingItems != null && !pricingItems.isEmpty();
        }
        return true;
    }

    @Schema(description = "计费项")
    @Data
    public static class PricingItemCreateReq {

        @Schema(description = "计费单位", example = "mtokens")
        @NotNull(message = "计费单位不能为空")
        private UnitEnum unit;

        @Schema(description = "输入单价", example = "1.50")
        @NotNull(message = "输入单价不能为空")
        @DecimalMin(value = "0.0", message = "单价不能为负数")
        private BigDecimal priceInput;

        @Schema(description = "输出单价", example = "3.00")
        @NotNull(message = "输出单价不能为空")
        @DecimalMin(value = "0.0", message = "单价不能为负数")
        private BigDecimal priceOutput;
    }

}
