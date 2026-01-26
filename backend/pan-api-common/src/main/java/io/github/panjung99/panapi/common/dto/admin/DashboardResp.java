package io.github.panjung99.panapi.common.dto.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardResp {

    private Integer userCount;

    private Integer orderCount;

    private BigDecimal revenue;
}
