package io.github.panjung99.panapi.user.dao;

import io.github.panjung99.panapi.user.entity.Balance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface BalanceMapper {

    int insert(Balance balance);

    int update(Balance balance);

    Balance findByUserId(@Param("userId") Long userId);

    int addBalance(@Param("userId") Long userId,
                   @Param("amount") BigDecimal amount);

    int deductBalance(@Param("userId") Long userId,
                      @Param("amount") BigDecimal amount);

    int increaseBalance(@Param("userId") Long userId,
                      @Param("amount") BigDecimal amount);

    int transferBalance(@Param("fromUserId") Long fromUserId,
                        @Param("toUserId") Long toUserId,
                        @Param("amount") BigDecimal amount);
}