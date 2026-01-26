package io.github.panjung99.panapi.vendor.dao;

import io.github.panjung99.panapi.common.dto.admin.VendorResp;
import io.github.panjung99.panapi.vendor.entity.Vendor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface VendorMapper {

    /**
     * 根据ID查询服务商信息
     * @param id 服务商ID
     * @return 服务商实体
     */
    Vendor selectById(@Param("id") Long id);

    /**
     * 查询所有服务商及其Token
     * @return 服务商及其Token集合
     */
    List<VendorResp> selectVendorsWithTokens();

    /**
     * 查询所有有效服务商
     * @return 服务商列表
     */
    List<Vendor> selectAllActive();

    /**
     * 插入服务商信息
     * @param vendor 服务商实体
     * @return 影响行数
     */
    int insert(Vendor vendor);

    /**
     * 更新服务商余额
     * @param id 服务商ID
     * @param newBalance 新余额
     * @return 影响行数
     */
    int updateBalance(@Param("id") Long id, @Param("newBalance") BigDecimal newBalance);


    /**
     * 逻辑删除服务商
     * @param id 服务商ID
     * @return 影响行数
     */
    int logicalDelete(@Param("id") Long id);
}
