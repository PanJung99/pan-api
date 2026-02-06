package io.github.panjung99.panapi.common.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户查询请求")
public class AdminUserReq {

    @Schema(description = "当前页码", defaultValue = "1", example = "1")
    private int pageNum = 1;

    @Schema(description = "每页条数", defaultValue = "10", example = "10")
    private int pageSize = 10;

}
