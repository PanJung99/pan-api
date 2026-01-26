package io.github.panjung99.panapi.common.dto.be;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskQueryReq {
    private int pageNum = 1;      // 默认第1页
    private int pageSize = 10;    // 默认每页10条
}
