package io.github.panjung99.panapi.user.dao;

import io.github.panjung99.panapi.user.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

    Admin selectByUserId(Long userId);

    int insert(Admin admin);

    int update(Admin admin);

    int deleteById(Long id);
}