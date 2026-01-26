package io.github.panjung99.panapi.common.dto.admin;

import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "服务商 响应对象")
public class VendorResp {

    @Schema(description = "服务商ID", example = "5")
    private Long id;

    @Schema(description = "服务商名称", example = "OpenAI")
    private String name;

    @Schema(description = "API基础地址", example = "https://api.openai.com/v1")
    private String apiBaseUrl;

    @Schema(description = "服务商类型", example = "OPENAI")
    private VenTypeEnum venType;

    private List<VendorTokenReq> tokens;  // 一对多 Token 列表
}
