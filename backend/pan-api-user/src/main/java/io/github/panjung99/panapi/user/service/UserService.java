package io.github.panjung99.panapi.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.panjung99.panapi.common.dto.admin.AdminUserResp;
import io.github.panjung99.panapi.user.dao.AdminMapper;
import io.github.panjung99.panapi.user.dao.UserMapper;
import io.github.panjung99.panapi.user.entity.Admin;
import io.github.panjung99.panapi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final AdminMapper adminMapper;

    public User findById(Long id) {
        User user = userMapper.findById(id);
        return user;
    }

    public Admin getAdminByUserId(Long userId) {
        return adminMapper.selectByUserId(userId);
    }

    public IPage<AdminUserResp> getUserPage(int pageNum, int pageSize) {
        int MAX_PAGE_SIZE = 100; // 最大页大小
        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        if (pageNum < 1) {
            pageNum = 1;
        }
        Page<User> page = new Page<>(pageNum, pageSize);
        return userMapper.findUserPage(page);
    }

}
