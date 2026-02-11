package io.github.panjung99.panapi.user.service;

import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.user.dao.BalanceMapper;
import io.github.panjung99.panapi.user.dao.UserMapper;
import io.github.panjung99.panapi.user.entity.Balance;
import io.github.panjung99.panapi.user.entity.Bill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserBalanceService 测试")
class UserBalanceServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private BalanceMapper balanceMapper;

    @Mock
    private BillService billService;

    @InjectMocks
    private UserBalanceService userBalanceService;

    private Balance testBalance;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2026, 1, 1, 12, 0, 0);

        testBalance = new Balance();
        testBalance.setId(1L);
        testBalance.setUserId(1L);
        testBalance.setCurrentBalance(new BigDecimal("100.00"));
        testBalance.setCreatedAt(testDateTime);
        testBalance.setUpdatedAt(testDateTime);
    }

    @Test
    @DisplayName("根据用户ID获取余额 - 成功")
    void getBalanceByUserId_Success() {
        when(balanceMapper.findByUserId(1L)).thenReturn(testBalance);

        BigDecimal result = userBalanceService.getBalanceByUserId(1L);

        assertNotNull(result);
        assertEquals(new BigDecimal("100.00"), result);
        verify(balanceMapper, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("根据用户ID获取余额 - 余额不存在")
    void getBalanceByUserId_BalanceNotFound() {
        when(balanceMapper.findByUserId(999L)).thenReturn(null);

        AppException exception = assertThrows(AppException.class, () -> {
            userBalanceService.getBalanceByUserId(999L);
        });

        assertEquals(ErrorEnum.BALANCE_DATA_INCONSISTENT, exception.getError());
        verify(balanceMapper, times(1)).findByUserId(999L);
    }

    @Test
    @DisplayName("根据用户ID获取余额 - 多个不同用户")
    void getBalanceByUserId_MultipleUsers() {
        Balance balance2 = new Balance();
        balance2.setId(2L);
        balance2.setUserId(2L);
        balance2.setCurrentBalance(new BigDecimal("200.00"));

        when(balanceMapper.findByUserId(1L)).thenReturn(testBalance);
        when(balanceMapper.findByUserId(2L)).thenReturn(balance2);
        when(balanceMapper.findByUserId(3L)).thenReturn(null);

        BigDecimal result1 = userBalanceService.getBalanceByUserId(1L);
        BigDecimal result2 = userBalanceService.getBalanceByUserId(2L);

        assertEquals(new BigDecimal("100.00"), result1);
        assertEquals(new BigDecimal("200.00"), result2);

        assertThrows(AppException.class, () -> {
            userBalanceService.getBalanceByUserId(3L);
        });

        verify(balanceMapper, times(1)).findByUserId(1L);
        verify(balanceMapper, times(1)).findByUserId(2L);
        verify(balanceMapper, times(1)).findByUserId(3L);
    }

    @Test
    @DisplayName("扣除余额 - 成功")
    void deductBalance_Success() {
        when(balanceMapper.deductBalance(1L, new BigDecimal("10.00"))).thenReturn(1);

        userBalanceService.deductBalance(1L, new BigDecimal("10.00"));

        verify(balanceMapper, times(1)).deductBalance(1L, new BigDecimal("10.00"));
    }

    @Test
    @DisplayName("扣除余额 - 扣除失败")
    void deductBalance_Fail() {
        when(balanceMapper.deductBalance(1L, new BigDecimal("10.00"))).thenReturn(0);

        AppException exception = assertThrows(AppException.class, () -> {
            userBalanceService.deductBalance(1L, new BigDecimal("10.00"));
        });

        assertEquals(ErrorEnum.BALANCE_DATA_INCONSISTENT, exception.getError());
        verify(balanceMapper, times(1)).deductBalance(1L, new BigDecimal("10.00"));
    }

    @Test
    @DisplayName("扣除余额 - 不同金额")
    void deductBalance_DifferentAmounts() {
        when(balanceMapper.deductBalance(1L, new BigDecimal("1.00"))).thenReturn(1);
        when(balanceMapper.deductBalance(1L, new BigDecimal("100.00"))).thenReturn(1);
        when(balanceMapper.deductBalance(1L, new BigDecimal("0.01"))).thenReturn(1);

        userBalanceService.deductBalance(1L, new BigDecimal("1.00"));
        userBalanceService.deductBalance(1L, new BigDecimal("100.00"));
        userBalanceService.deductBalance(1L, new BigDecimal("0.01"));

        verify(balanceMapper, times(1)).deductBalance(1L, new BigDecimal("1.00"));
        verify(balanceMapper, times(1)).deductBalance(1L, new BigDecimal("100.00"));
        verify(balanceMapper, times(1)).deductBalance(1L, new BigDecimal("0.01"));
    }

    @Test
    @DisplayName("增加余额 - 成功")
    void increaseBalance_Success() {
        when(balanceMapper.increaseBalance(1L, new BigDecimal("50.00"))).thenReturn(1);

        userBalanceService.increaseBalance(1L, new BigDecimal("50.00"));

        verify(balanceMapper, times(1)).increaseBalance(1L, new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("增加余额 - 增加失败")
    void increaseBalance_Fail() {
        when(balanceMapper.increaseBalance(1L, new BigDecimal("50.00"))).thenReturn(0);

        AppException exception = assertThrows(AppException.class, () -> {
            userBalanceService.increaseBalance(1L, new BigDecimal("50.00"));
        });

        assertEquals(ErrorEnum.BALANCE_DATA_INCONSISTENT, exception.getError());
        verify(balanceMapper, times(1)).increaseBalance(1L, new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("增加余额 - 不同金额")
    void increaseBalance_DifferentAmounts() {
        when(balanceMapper.increaseBalance(1L, new BigDecimal("1.00"))).thenReturn(1);
        when(balanceMapper.increaseBalance(1L, new BigDecimal("1000.00"))).thenReturn(1);
        when(balanceMapper.increaseBalance(1L, new BigDecimal("0.50"))).thenReturn(1);

        userBalanceService.increaseBalance(1L, new BigDecimal("1.00"));
        userBalanceService.increaseBalance(1L, new BigDecimal("1000.00"));
        userBalanceService.increaseBalance(1L, new BigDecimal("0.50"));

        verify(balanceMapper, times(1)).increaseBalance(1L, new BigDecimal("1.00"));
        verify(balanceMapper, times(1)).increaseBalance(1L, new BigDecimal("1000.00"));
        verify(balanceMapper, times(1)).increaseBalance(1L, new BigDecimal("0.50"));
    }

    @Test
    @DisplayName("手动调整余额 - 增加余额成功")
    void adjustBalance_Increase_Success() {
        when(balanceMapper.findByUserId(1L)).thenReturn(testBalance);
        when(balanceMapper.increaseBalance(1L, new BigDecimal("50.00"))).thenReturn(1);
        doNothing().when(billService).createBill(
            eq(Bill.BillType.MANUAL_ADJUSTMENT),
            eq(new BigDecimal("50.00")),
            isNull(),
            eq(1L),
            isNull(),
            eq("测试充值")
        );

        userBalanceService.adjustBalance(1L, new BigDecimal("50.00"), "测试充值");

        verify(balanceMapper, times(1)).findByUserId(1L);
        verify(balanceMapper, times(1)).increaseBalance(1L, new BigDecimal("50.00"));
        verify(balanceMapper, never()).deductBalance(anyLong(), any(BigDecimal.class));
        verify(billService, times(1)).createBill(
            eq(Bill.BillType.MANUAL_ADJUSTMENT),
            eq(new BigDecimal("50.00")),
            isNull(),
            eq(1L),
            isNull(),
            eq("测试充值")
        );
    }

    @Test
    @DisplayName("手动调整余额 - 扣除余额成功")
    void adjustBalance_Deduct_Success() {
        when(balanceMapper.findByUserId(1L)).thenReturn(testBalance);
        when(balanceMapper.deductBalance(1L, new BigDecimal("30.00"))).thenReturn(1);
        doNothing().when(billService).createBill(
            eq(Bill.BillType.MANUAL_ADJUSTMENT),
            eq(new BigDecimal("-30.00")),
            isNull(),
            eq(1L),
            isNull(),
            eq("测试扣费")
        );

        userBalanceService.adjustBalance(1L, new BigDecimal("-30.00"), "测试扣费");

        verify(balanceMapper, times(1)).findByUserId(1L);
        verify(balanceMapper, times(1)).deductBalance(1L, new BigDecimal("30.00"));
        verify(balanceMapper, never()).increaseBalance(anyLong(), any(BigDecimal.class));
        verify(billService, times(1)).createBill(
            eq(Bill.BillType.MANUAL_ADJUSTMENT),
            eq(new BigDecimal("-30.00")),
            isNull(),
            eq(1L),
            isNull(),
            eq("测试扣费")
        );
    }

    @Test
    @DisplayName("手动调整余额 - 余额不存在")
    void adjustBalance_BalanceNotFound() {
        when(balanceMapper.findByUserId(999L)).thenReturn(null);

        AppException exception = assertThrows(AppException.class, () -> {
            userBalanceService.adjustBalance(999L, new BigDecimal("50.00"), "测试");
        });

        assertEquals(ErrorEnum.BALANCE_DATA_INCONSISTENT, exception.getError());
        verify(balanceMapper, times(1)).findByUserId(999L);
        verify(balanceMapper, never()).increaseBalance(anyLong(), any(BigDecimal.class));
        verify(balanceMapper, never()).deductBalance(anyLong(), any(BigDecimal.class));
        verify(billService, never()).createBill(any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("手动调整余额 - 原因为空时使用默认值")
    void adjustBalance_NullReason() {
        when(balanceMapper.findByUserId(1L)).thenReturn(testBalance);
        when(balanceMapper.increaseBalance(1L, new BigDecimal("20.00"))).thenReturn(1);
        doNothing().when(billService).createBill(
            eq(Bill.BillType.MANUAL_ADJUSTMENT),
            eq(new BigDecimal("20.00")),
            isNull(),
            eq(1L),
            isNull(),
            eq("人工调整")
        );

        userBalanceService.adjustBalance(1L, new BigDecimal("20.00"), null);

        verify(balanceMapper, times(1)).findByUserId(1L);
        verify(balanceMapper, times(1)).increaseBalance(1L, new BigDecimal("20.00"));
        verify(billService, times(1)).createBill(
            eq(Bill.BillType.MANUAL_ADJUSTMENT),
            eq(new BigDecimal("20.00")),
            isNull(),
            eq(1L),
            isNull(),
            eq("人工调整")
        );
    }

    @Test
    @DisplayName("手动调整余额 - 零金额增加")
    void adjustBalance_ZeroAmount() {
        when(balanceMapper.findByUserId(1L)).thenReturn(testBalance);
        when(balanceMapper.increaseBalance(1L, new BigDecimal("0.00"))).thenReturn(1);
        doNothing().when(billService).createBill(
            any(Bill.BillType.class),
            any(BigDecimal.class),
            isNull(),
            eq(1L),
            isNull(),
            anyString()
        );

        userBalanceService.adjustBalance(1L, new BigDecimal("0.00"), "零金额测试");

        verify(balanceMapper, times(1)).findByUserId(1L);
        verify(balanceMapper, times(1)).increaseBalance(1L, new BigDecimal("0.00"));
        verify(billService, times(1)).createBill(any(Bill.BillType.class), any(BigDecimal.class), isNull(), eq(1L), isNull(), anyString());
    }

    @Test
    @DisplayName("手动调整余额 - 大额调整")
    void adjustBalance_LargeAmount() {
        when(balanceMapper.findByUserId(1L)).thenReturn(testBalance);
        when(balanceMapper.increaseBalance(1L, new BigDecimal("10000.00"))).thenReturn(1);
        doNothing().when(billService).createBill(
            eq(Bill.BillType.MANUAL_ADJUSTMENT),
            eq(new BigDecimal("10000.00")),
            isNull(),
            eq(1L),
            isNull(),
            eq("大额充值")
        );

        userBalanceService.adjustBalance(1L, new BigDecimal("10000.00"), "大额充值");

        verify(balanceMapper, times(1)).findByUserId(1L);
        verify(balanceMapper, times(1)).increaseBalance(1L, new BigDecimal("10000.00"));
        verify(billService, times(1)).createBill(
            eq(Bill.BillType.MANUAL_ADJUSTMENT),
            eq(new BigDecimal("10000.00")),
            isNull(),
            eq(1L),
            isNull(),
            eq("大额充值")
        );
    }

    @Test
    @DisplayName("完整流程 - 获取余额、增加余额、扣除余额、手动调整")
    void fullWorkflow_BalanceOperations() {
        when(balanceMapper.findByUserId(1L)).thenReturn(testBalance);
        when(balanceMapper.increaseBalance(1L, new BigDecimal("50.00"))).thenReturn(1);
        when(balanceMapper.deductBalance(1L, new BigDecimal("20.00"))).thenReturn(1);
        when(balanceMapper.increaseBalance(1L, new BigDecimal("30.00"))).thenReturn(1);
        doNothing().when(billService).createBill(
            eq(Bill.BillType.MANUAL_ADJUSTMENT),
            any(BigDecimal.class),
            isNull(),
            eq(1L),
            isNull(),
            anyString()
        );

        BigDecimal balance = userBalanceService.getBalanceByUserId(1L);
        assertNotNull(balance);
        assertEquals(new BigDecimal("100.00"), balance);

        userBalanceService.increaseBalance(1L, new BigDecimal("50.00"));

        userBalanceService.deductBalance(1L, new BigDecimal("20.00"));

        userBalanceService.adjustBalance(1L, new BigDecimal("30.00"), "完整流程测试");

        verify(balanceMapper, times(2)).findByUserId(1L);
        verify(balanceMapper, times(1)).increaseBalance(1L, new BigDecimal("50.00"));
        verify(balanceMapper, times(1)).increaseBalance(1L, new BigDecimal("30.00"));
        verify(balanceMapper, times(1)).deductBalance(1L, new BigDecimal("20.00"));
        verify(billService, times(1)).createBill(
            eq(Bill.BillType.MANUAL_ADJUSTMENT),
            eq(new BigDecimal("30.00")),
            isNull(),
            eq(1L),
            isNull(),
            eq("完整流程测试")
        );
    }
}
