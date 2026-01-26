package io.github.panjung99.panapi.common.dto.be;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillStatsDto {

    private BillStats today;

    private BillStats month;

    @Data
    public static class BillStats {
        private BigDecimal totalAmount;
        private Long transactionCount;
    }
}

