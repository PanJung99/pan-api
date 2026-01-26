package io.github.panjung99.panapi.web.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ToBQueryMapper {
    // 查询用户数量
    Integer queryUserCount();

    // 查询订单数量
    Integer queryOrderCount(@Param("startDate") LocalDateTime startDate,
                            @Param("endDate") LocalDateTime endDate);

    // 查询总账单
    BigDecimal queryRevenue(@Param("startDate") LocalDateTime startDate,
                            @Param("endDate") LocalDateTime endDate);
}