package io.github.panjung99.panapi.user.service;

import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.user.dao.BalanceMapper;
import io.github.panjung99.panapi.user.dao.UserMapper;
import io.github.panjung99.panapi.user.entity.Balance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
public class UserBalanceService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    /**
     * 通过userId获取用户余额
     * @param userId
     * @return 用户当前余额
     */
    public BigDecimal getBalanceByUserId(Long userId) {
        Balance balance = balanceMapper.findByUserId(userId);
        if (balance == null) {
            throw new AppException(ErrorEnum.BALANCE_DATA_INCONSISTENT);
        }
        return balance.getCurrentBalance();
    }

    /**
     * 扣除余额， 注意要同时在Bill表创建扣费记录
     * @param userId
     * @param amount
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
     * 增加余额， 注意要同时在Bill表创建扣费记录
     * @param userId
     * @param amount
     */
    @Transactional(rollbackFor = Exception.class)
    public void increaseBalance(Long userId, BigDecimal amount) {
        int count = balanceMapper.increaseBalance(userId, amount);
        if (count < 1) {
            throw new AppException(ErrorEnum.BALANCE_DATA_INCONSISTENT);
        }
    }
}
