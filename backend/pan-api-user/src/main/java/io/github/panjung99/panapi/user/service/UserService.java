package io.github.panjung99.panapi.user.service;


import io.github.panjung99.panapi.user.dao.AdminMapper;
import io.github.panjung99.panapi.user.dao.UserMapper;
import io.github.panjung99.panapi.user.entity.Admin;
import io.github.panjung99.panapi.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserBalanceService userBalanceService;

    @Autowired
    private ApiKeyService apiKeyService;


    public User findById(Long id) {
        User user = userMapper.findById(id);
        return user;
    }

    public Admin getAdminByUserId(Long userId) {
        return adminMapper.selectByUserId(userId);
    }


}
