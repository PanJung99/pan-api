package io.github.panjung99.panapi.common.dto.be;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargePlanResp {

    private Long planId;

    private String name;

    private BigDecimal price;

    private String desc;

    private String displayMd;

    private Integer isRecommend;

    private Integer sortOrder;

    private Integer status;

}
