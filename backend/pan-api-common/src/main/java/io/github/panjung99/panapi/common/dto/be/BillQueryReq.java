package io.github.panjung99.panapi.common.dto.be;

import lombok.Data;

import java.time.LocalDateTime;

// 查询DTO
@Data
public class BillQueryReq {

    private String billType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}