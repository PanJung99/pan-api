package io.github.panjung99.panapi.vendor.dao;

import io.github.panjung99.panapi.vendor.entity.VendorModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VendorModelMapper {

    /**
     * 根据ID查询模型
     * @param id 模型ID
     * @return 模型实体
     */
    VendorModel selectById(@Param("id") Long id);


    /**
     * 查询全部未删除模型
     * @return 模型列表
     */
    List<VendorModel> selectAll();

    /**
     * 根据服务商ID查询模型
     * @param vendorId 服务商ID
     * @return 模型列表
     */
    List<VendorModel> selectAllByVendorId(@Param("vendorId") Long vendorId);

    /**
     * 插入模型信息
     * @param model 模型实体
     * @return 影响行数
     */
    int insert(VendorModel model);

    /**
     * 更新模型信息
     * @param model 模型实体
     * @return 影响行数
     */
    int update(VendorModel model);

    /**
     * 逻辑删除模型
     * @param id 模型ID
     * @return 影响行数
     */
    int logicalDelete(@Param("id") Long id, @Param("date") LocalDateTime date);
}
