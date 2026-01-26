package io.github.panjung99.panapi.web.service;


import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.admin.DashboardResp;
import io.github.panjung99.panapi.common.util.DateUtil;
import io.github.panjung99.panapi.web.dao.ToBQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AdminService {

    @Autowired
    private ToBQueryMapper toBQueryMapper;

    public ResponseDto<DashboardResp> dashboardQuery(LocalDateTime dateTime) {
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
        }

        // 转换为月初（当月第一天的00:00:00）
        LocalDateTime startOfMonth = DateUtil.getStartOfMonth(dateTime);

        // 转换为月末（当月最后一天的23:59:59.999999999）
        LocalDateTime endOfMonth = DateUtil.getEndOfMonth(dateTime);

        DashboardResp respDto = new DashboardResp();
        respDto.setUserCount(toBQueryMapper.queryUserCount());
        respDto.setOrderCount(toBQueryMapper.queryOrderCount(startOfMonth, endOfMonth));

        BigDecimal revenue = toBQueryMapper.queryRevenue(startOfMonth, endOfMonth);
        if (revenue != null) {
            revenue = revenue.negate();
        } else {
            revenue = BigDecimal.ZERO;
        }
        respDto.setRevenue(revenue);

        return ResponseDto.getSuccessResponse(respDto);

    }

}