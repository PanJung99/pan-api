package io.github.panjung99.panapi.vendor.dao;

import io.github.panjung99.panapi.vendor.entity.VendorToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VendorTokenMapper {

    List<VendorToken> selectByVendorAndKey(@Param("vendorId") Long vendorId, @Param("apiKey") String key);

    List<VendorToken> selectByVendorId(@Param("vendorId") Long vendorId);

    List<VendorToken> selectActiveByVendorId(@Param("vendorId") Long vendorId);

    // 插入新Token
    int insert(VendorToken vendorToken);

    // 根据ID更新Token
    int update(VendorToken vendorToken);

    // 根据ID删除Token
    int deleteById(Long id);

    // 根据ID查询Token
    VendorToken findById(Long id);

}