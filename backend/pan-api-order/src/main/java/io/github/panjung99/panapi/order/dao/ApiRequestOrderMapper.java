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


    /**
     * 更新订单同步状态
     * @param reqId 请求ID
     * @param syncStatus 同步状态
     * @param errorMessage 错误信息
     * @param lastSyncTime 最后同步时间
     * @return 更新记录数
     */
    int updateSyncStatus(@Param("reqId") String reqId,
                         @Param("syncStatus") Integer syncStatus,
                         @Param("errorMessage") String errorMessage,
                         @Param("lastSyncTime") LocalDateTime lastSyncTime);

    /**
     * 更新订单金额和响应内容
     * @param reqId 请求ID
     * @param orderAmount 订单金额
     * @param respContent 响应内容
     * @return 更新记录数
     */
    int updateOrderAmount(@Param("reqId") String reqId,
                          @Param("orderAmount") BigDecimal orderAmount,
                          @Param("respContent") String respContent);

    /**
     * 更新订单重试次数
     * @param reqId 请求ID
     * @param retryCount 重试次数
     * @return 更新记录数
     */
    int updateRetryCount(@Param("reqId") String reqId,
                         @Param("retryCount") Integer retryCount);



    /**
     * 更新订单状态、重试次数和错误信息
     */
    int updateSyncStatusAndRetryCount(@Param("reqId") String reqId, @Param("syncStatus") Integer syncStatus, @Param("retryCount") Integer retryCount, @Param("errorMessage") String errorMessage, @Param("updatedAt") LocalDateTime updatedAt);

}
