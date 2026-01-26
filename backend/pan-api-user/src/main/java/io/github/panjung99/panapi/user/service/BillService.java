package io.github.panjung99.panapi.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.panjung99.panapi.common.dto.be.BillQueryReq;
import io.github.panjung99.panapi.common.dto.be.BillStatsDto;
import io.github.panjung99.panapi.common.dto.be.BillVO;
import io.github.panjung99.panapi.common.util.DateUtil;
import io.github.panjung99.panapi.user.dao.BillMapper;
import io.github.panjung99.panapi.user.entity.Bill;
import io.github.panjung99.panapi.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BillService {

    @Autowired
    private BillMapper billMapper;

    public void createBill(Bill.BillType type, BigDecimal amount, Long apiKeyId, Long userId, String relatedId, String desc) {
        createBill(type, amount, apiKeyId, userId, relatedId, desc, null, null);
    }

    public void createBill(Bill.BillType type, BigDecimal amount, Long apiKeyId, Long userId, String relatedId, String desc, Long modelTagId, String modelTagName) {
        Bill bill = new Bill();
        bill.setBillType(type.getCode());
        bill.setAmount(amount);
        bill.setApiKeyId(apiKeyId);
        bill.setUserId(userId);
        bill.setRelatedId(relatedId);
        bill.setDescription(desc);
        bill.setModelTagId(modelTagId);
        bill.setModelTagName(modelTagName);
        bill.setCreateTime(LocalDateTime.now());
        billMapper.insert(bill);
    }

    /**
     * 条件分页查询用户账单
     */
    public IPage<BillVO> getBillVOPageByCondition(User user, BillQueryReq req) {
        Page<BillVO> page = new Page<>(req.getPageNum(), req.getPageSize());
        IPage<BillVO> billVOPage = billMapper.selectBillVOPageByCondition(page, user.getId(), req.getBillType(),
                                                                            req.getStartTime(), req.getEndTime());

        return billVOPage;
    }

    public BillStatsDto getBillStats(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monthStart = DateUtil.getStartOfMonth(now);
        LocalDateTime monthEnd = DateUtil.getEndOfMonth(now);

        // 统计今日数据
        LocalDateTime todayStart = DateUtil.getStartOfDay(now);
        LocalDateTime todayEnd = DateUtil.getEndOfDay(now);

        // 执行查询
        BillStatsDto.BillStats monthStats = billMapper.selectConsumptionStatsByTimeRange(user.getId(), monthStart, monthEnd);
        BillStatsDto.BillStats todayStats = billMapper.selectConsumptionStatsByTimeRange(user.getId(), todayStart, todayEnd);

        BillStatsDto dto = new BillStatsDto();
        dto.setToday(todayStats);
        dto.setMonth(monthStats);

        return dto;
    }

}
