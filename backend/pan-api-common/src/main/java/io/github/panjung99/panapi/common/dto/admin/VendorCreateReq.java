package io.github.panjung99.panapi.common.dto.admin;

import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "服务商创建请求")
public class VendorCreateReq {

    @Schema(description = "服务商名称", example = "DeepSeek-高可用渠道", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "服务商名称不能为空")
    private String name;

    @Schema(description = "API基础地址", example = "https://api.deepseek.com/v1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "服务商名称不能为空")
    private String apiBaseUrl;

    @Schema(description = "服务商类型", example = "DEEP_SEEK", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "服务商类型不能为空")
    private VenTypeEnum venType;
}
