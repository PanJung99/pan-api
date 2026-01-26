package io.github.panjung99.panapi.common.dto.be;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BillVO {

    private Long id;
    private String billType;
    private BigDecimal amount;
    private String relatedId;
    private String description;
    private Long modelTagId;
    private String modelTagName;
    private LocalDateTime createTime;

}