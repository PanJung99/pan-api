package io.github.panjung99.panapi.user.service;

import io.github.panjung99.panapi.common.dto.be.LoginReq;
import io.github.panjung99.panapi.common.dto.be.RegisterReq;
import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.user.dao.BalanceMapper;
import io.github.panjung99.panapi.user.dao.UserMapper;
import io.github.panjung99.panapi.user.entity.Balance;
import io.github.panjung99.panapi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;

    private final BalanceMapper balanceMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional(rollbackFor = Exception.class)
    public User register(RegisterReq request) {
        // 检查邮箱是否唯一
        if (request.getEmail() != null &&
                userMapper.findByEmail(request.getEmail()) != null) {
            throw new AppException(ErrorEnum.EMAIL_ALREADY_EXISTS);
        }

        // 加密密码(BCrypt自动加盐且包含在密文里)
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encryptedPassword);
        user.setEmail(request.getEmail());
        user.setLoginType(User.LoginType.PASSWORD.getCode());
        user.setDeleted(false);
        userMapper.insert(user);

        Balance balance = new Balance();
        balance.setUserId(user.getId());
        balance.setCurrentBalance(BigDecimal.ZERO);
        balanceMapper.insert(balance);

        return user;
    }


    public User login(LoginReq request) {
        // 根据登录标识查找用户
        User user = getUserByIdentifier(request.getIdentifier());
        if (user == null) {
            throw new AppException(ErrorEnum.NO_SUCH_USER);
        }

        // 验证密码
        boolean valid = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!valid) {
            throw new AppException(ErrorEnum.WRONG_IDENTIFIER);
        }
        return user;
    }

    private User getUserByIdentifier(String identifier) {
        // 尝试按手机号查找
        User user = userMapper.findByPhone(identifier);
        if (user != null) {
            return user;
        }
        // 尝试按邮箱查找
        return userMapper.findByEmail(identifier);
    }


}
