package io.github.panjung99.panapi.order.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.panjung99.panapi.common.dto.admin.ApiOrderResp;
import io.github.panjung99.panapi.order.entity.ApiRequestOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ApiRequestOrderMapper {

    int insert(ApiRequestOrder order);

    ApiRequestOrder selectByReqId(String reqId);

    long sumTodayTokenByUserId(Long userId);

    long sumTodayRequestByUserId(Long userId);


    /**
     * 查询订单与消费记录
     * @return List<ApiOrderVO>
     */
    IPage<ApiOrderResp> selectApiOrderList(@Param("page") Page<ApiOrderResp> page,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);




}
