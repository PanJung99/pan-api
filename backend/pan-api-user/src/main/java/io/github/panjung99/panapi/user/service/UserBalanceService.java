package io.github.panjung99.panapi.user.service;

import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.user.dao.BalanceMapper;
import io.github.panjung99.panapi.user.dao.UserMapper;
import io.github.panjung99.panapi.user.entity.Balance;
import io.github.panjung99.panapi.user.entity.Bill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBalanceService {

    private final UserMapper userMapper;

    private final BalanceMapper balanceMapper;

    private final BillService billService;

    /**
     * Retrieves the balance of a specific user.
     * @param userId user id
     * @return user's current balance
     */
    public BigDecimal getBalanceByUserId(Long userId) {
        Balance balance = balanceMapper.findByUserId(userId);
        if (balance == null) {
            throw new AppException(ErrorEnum.BALANCE_DATA_INCONSISTENT);
        }
        return balance.getCurrentBalance();
    }

    /**
     * Deducts the balance of a specific user.
     * Note: Remember to create a bill record.
     * @param userId user id
     * @param amount amount to deduct
     */
    @Transactional(rollbackFor = Exception.class)
    public void deductBalance(Long userId, BigDecimal amount) {
        int count = balanceMapper.deductBalance(userId, amount);
        if (count < 1) {
            log.error("deductBalance fail, userId: {}, amount: {}", userId, amount);
            throw new AppException(ErrorEnum.BALANCE_DATA_INCONSISTENT);
        }
    }

    /**
     * Increases the balance of a specific user.
     * Note: Remember to create a bill record.
     * @param userId user id
     * @param amount amount to increase
     */
    @Transactional(rollbackFor = Exception.class)
    public void increaseBalance(Long userId, BigDecimal amount) {
        int count = balanceMapper.increaseBalance(userId, amount);
        if (count < 1) {
            throw new AppException(ErrorEnum.BALANCE_DATA_INCONSISTENT);
        }
    }

    /**
     * Manually adjusts the balance of a specific user(increase or deduct).
     * And create a bill record.
     * @param userId user id
     * @param amount amount to increase/deduct (positive to increase)
     * @param reason reason why adjust
     */
    @Transactional(rollbackFor = Exception.class)
    public void adjustBalance(Long userId, BigDecimal amount, String reason) {
        Balance balance = balanceMapper.findByUserId(userId);
        if (balance == null) {
            throw new AppException(ErrorEnum.BALANCE_DATA_INCONSISTENT);
        }

        BigDecimal deductAmount = amount.abs();
        if (amount.compareTo(BigDecimal.ZERO) >= 0) {
            increaseBalance(userId, deductAmount);
        } else {
            deductBalance(userId, deductAmount);
        }

        billService.createBill(
            Bill.BillType.MANUAL_ADJUSTMENT,
            amount,
            null,
            userId,
            null,
            reason != null ? reason : "人工调整"
        );
        log.info("Manual balance adjustment - userId: {}, amount: {}, reason: {}",
            userId, amount, reason);
    }
}
