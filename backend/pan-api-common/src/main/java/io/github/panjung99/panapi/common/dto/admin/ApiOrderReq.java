package io.github.panjung99.panapi.common.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "API订单查询请求")
public class ApiOrderReq {

    @Schema(description = "当前页码", defaultValue = "1", example = "1")
    private int pageNum = 1;

    @Schema(description = "每页条数", defaultValue = "10", example = "10")
    private int pageSize = 10;

    @Schema(description = "开始时间", example = "2026-01-17 00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "结束时间", example = "2026-01-31 23:59:59")
    private LocalDateTime endDate;
}