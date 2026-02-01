package io.github.panjung99.panapi.common.dto.be;

import io.github.panjung99.panapi.common.enums.ModelCategory;
import io.github.panjung99.panapi.common.enums.UnitEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "模型展示详情对象（包含映射关系）")
public class ModelResp {

    @Schema(description = "模型ID", example = "1")
    private Long id;

    @Schema(description = "模型唯一标识名称", example = "gpt-4-turbo")
    private String name;

    @Schema(description = "对外展示的模型名称", example = "ChatGPT 4 Turbo")
    private String displayName;

    @Schema(description = "是否免费", example = "false")
    private Boolean isFree;

    @Schema(description = "模型类别：chat-对话, image-绘画, audio-语音, video-视频, embedding-向量")
    private ModelCategory category;

    @Schema(description = "模型描述信息", example = "这是目前最强大的语言模型之一")
    private String description;

    @Schema(description = "状态：0-禁用, 1-启用", example = "1")
    private Integer isActive;

    @Schema(description = "绑定的服务商模型映射列表(用户端不包含)")
    private List<BindingDetail> bindings;

    @Schema(description = "模型计费项列表")
    private List<PricingItemResp> pricingItems;

    @Data
    @Schema(description = "模型绑定详情（用户端不包含该字段）")
    public static class BindingDetail {

        @Schema(description = "绑定关系ID", example = "1001")
        private Long id;

        @Schema(description = "平台模型ID", example = "1")
        private Long modelId;

        @Schema(description = "服务商原始模型ID（关联 ven_model 表）", example = "50")
        private Long venModelId;

        @Schema(description = "绑定是否启用：0-禁用, 1-启用", example = "1")
        private Integer enabled;
    }

    @Data
    @Schema(description = "模型计费项详情")
    public static class PricingItemResp {
        @Schema(description = "计费项ID", example = "100")
        private Long id;

        @Schema(description = "计费单位", example = "mtokens")
        private UnitEnum unit;

        @Schema(description = "输入单价", example = "0.01")
        private BigDecimal priceInput;

        @Schema(description = "输出单价", example = "0.02")
        private BigDecimal priceOutput;

        @Schema(description = "货币类型", example = "CNY")
        private String currency;

        @Schema(description = "状态：0-禁用, 1-启用", example = "1")
        private Integer isActive;
    }
}