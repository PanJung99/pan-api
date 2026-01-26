package io.github.panjung99.panapi.common.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "服务商模型响应对象")
public class VendorModelResp {

    @Schema(description = "服务商模型ID (ven_model 表 ID)", example = "14")
    private Long id;

    @Schema(description = "服务商ID (关联 vendor 表)", example = "2")
    private Long vendorId;

    @Schema(description = "服务商内部模型名称 (如: gpt-4, deepseek-chat)", example = "gpt-4-0125-preview")
    private String name;
}