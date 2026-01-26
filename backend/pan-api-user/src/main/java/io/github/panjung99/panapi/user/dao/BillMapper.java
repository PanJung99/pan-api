package io.github.panjung99.panapi.user.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.panjung99.panapi.common.dto.be.BillStatsDto;
import io.github.panjung99.panapi.common.dto.be.BillVO;
import io.github.panjung99.panapi.user.entity.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BillMapper {
    int insert(Bill bill);
    List<Bill> findByUserId(@Param("userId") Long userId);
    List<Bill> findByApiKeyId(@Param("apiKeyId") Long apiKeyId);
    List<Bill> findByTypeAndDateRange(@Param("userId") Long userId,
                                      @Param("billType") Integer billType,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);
    BigDecimal sumAmountByUserAndType(@Param("userId") Long userId,
                                      @Param("billType") Integer billType);
    // 分页查询带条件的用户账单
    IPage<BillVO> selectBillVOPageByCondition(Page<BillVO> page,
                                              @Param("userId") Long userId,
                                              @Param("billType") String billType,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    BillStatsDto.BillStats selectConsumptionStatsByTimeRange(@Param("userId") Long userId,
                                                             @Param("startTime") LocalDateTime startTime,
                                                             @Param("endTime") LocalDateTime endTime);
}