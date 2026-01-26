package io.github.panjung99.panapi.user.dao;

import io.github.panjung99.panapi.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User user);

    int softDelete(@Param("id") Long id);

    User findById(@Param("id") Long id);



    User findByWechatOpenid(@Param("openid") String openid);

    User findByPhone(@Param("phone") String phone);

    User findByEmail(@Param("email") String email);

    List<User> findByLoginType(@Param("loginType") int loginType);

    int updateBalance(@Param("id") Long id,
                      @Param("amount") BigDecimal amount,
                      @Param("version") int version);

    int updatePassword(@Param("id") Long id,
                       @Param("password") String newPassword);
}