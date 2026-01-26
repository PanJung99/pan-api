package io.github.panjung99.panapi.common.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModelStatusUpdateReq {

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态", example = "true")
    private Boolean enabled;

}
