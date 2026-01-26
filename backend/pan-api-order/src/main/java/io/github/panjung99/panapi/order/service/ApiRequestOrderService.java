package io.github.panjung99.panapi.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.panjung99.panapi.common.dto.api.*;
import io.github.panjung99.panapi.common.dto.admin.ApiOrderResp;
import io.github.panjung99.panapi.common.enums.ModelCategory;
import io.github.panjung99.panapi.common.util.JsonUtil;


import io.github.panjung99.panapi.model.entity.Model;
import io.github.panjung99.panapi.order.dao.ApiRequestOrderMapper;
import io.github.panjung99.panapi.order.entity.ApiRequestOrder;
import io.github.panjung99.panapi.vendor.entity.VendorModel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ApiRequestOrderService {

    @Autowired
    private ApiRequestOrderMapper orderMapper;

    /**
     * 对话请求创建订单
     */
    @Transactional
    public void createOrder(String reqId,
                            HttpServletRequest request,
                            Boolean stream,
                            Usage usage,
                            Long userId,
                            Long apiKeyId,
                            Model model,
                            VendorModel chosenModel,
                            BigDecimal amount,
                            String vendorOrderNo) {
        ApiRequestOrder order = new ApiRequestOrder();

        // 设置租户ID和请求信息
        order.setReqId(reqId);
        order.setUserId(userId); // 实现用户认证
        order.setApiKeyId(apiKeyId);
        order.setClientIp(getClientIp(request));
        order.setReqPath(request.getRequestURI());
        order.setReqStream(stream);

        // 设置模型信息
        order.setModelId(model.getId());
        order.setModelType(ModelCategory.chat.name());

        // 设置响应信息
        order.setRespModelName(chosenModel.getName());
        order.setRespModelId(chosenModel.getId());
        order.setRespVendorId(chosenModel.getVendorId());


        // 设置其他响应数据
//        order.setFinishReason(responseBody.getChoices()[0].getFinishReason());
        order.setPromptTokens(usage.getPromptTokens());
        order.setCompletionTokens(usage.getCompletionTokens());
        order.setTotalTokens(usage.getTotalTokens());
//        order.setSystemFingerprint(request.get());
        order.setCreatedAt(LocalDateTime.now());
//        order.setLatencyMs((int) (System.currentTimeMillis() - startTime));
        order.setStatusCode(200);

        order.setOrderAmount(amount);
        order.setSyncStatus(2);
        order.setVendorOrderNo(vendorOrderNo);
        order.setSyncRetryCount(0);

        try {
            int result = orderMapper.insert(order);
            if (result != 1) {
                throw new RuntimeException("Failed to insert order");
            }
        } catch (Exception e) {
            log.error("Failed to save order. Req ID: {}", order.getReqId(), e);
            throw new RuntimeException("Order save failed", e);
        }
    }

    public Long getTodayTokenByUserId(Long userId) {
        return orderMapper.sumTodayTokenByUserId(userId);
    }

    public Long getTodayRequestByUserId(Long userId) {
        return orderMapper.sumTodayRequestByUserId(userId);
    }


    private static String getClientIp(HttpServletRequest request) {
        // 实现获取真实IP的逻辑
        return request.getRemoteAddr();
    }


    public ApiRequestOrder getByReqId(String reqId) {
        return orderMapper.selectByReqId(reqId);
    }

    public IPage<ApiOrderResp> getApiOrderList(int pageNum, int pageSize,
                                               LocalDateTime startDate, LocalDateTime endDate) {
        int MAX_PAGE_SIZE = 100; // 最大页大小
        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        if (pageNum < 1) {
            pageNum = 1;
        }
        Page<ApiOrderResp> page = new Page<>(pageNum, pageSize);
        return orderMapper.selectApiOrderList(page, startDate, endDate);
    }


    /**
     * 更新订单同步状态
     * 
     * @param reqId 请求ID
     * @param syncStatus 同步状态
     * @param errorMessage 错误信息（可选）
     */
    @Transactional
    public void updateSyncStatus(String reqId, Integer syncStatus, String errorMessage) {
        orderMapper.updateSyncStatus(reqId, syncStatus, errorMessage, LocalDateTime.now());
    }


    /**
     * 更新订单同步状态、重试次数和错误信息（用于异步任务失败时）
     *
     * @param reqId 请求ID
     * @param syncStatus 同步状态
     * @param retryCount 重试次数
     * @param errorMessage 错误信息
     */
    @Transactional
    public void updateSyncStatusAndRetryCount(String reqId, Integer syncStatus, Integer retryCount, String errorMessage) {
        orderMapper.updateSyncStatusAndRetryCount(reqId, syncStatus, retryCount, errorMessage, LocalDateTime.now());
    }

    /**
     * 更新订单重试次数
     * 
     * @param reqId 请求ID
     * @param retryCount 重试次数
     */
    @Transactional
    public void updateRetryCount(String reqId, Integer retryCount) {
        orderMapper.updateRetryCount(reqId, retryCount);
    }
}
